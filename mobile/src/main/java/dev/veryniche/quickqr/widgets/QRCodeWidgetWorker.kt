package dev.veryniche.quickqr.widgets

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.storage.QRCodesRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

@HiltWorker
class QRCodeWidgetWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val qrCodesRepository: QRCodesRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val manager = GlanceAppWidgetManager(appContext)
        val widget = QRCodeWidget()
        val glanceIds = manager.getGlanceIds(widget.javaClass)
        Timber.d("Updating widgets: $glanceIds")
        glanceIds.forEach { glanceId ->
            updateAppWidgetState(
                context = appContext,
                glanceId = glanceId,
            ) { prefs ->
                prefs.apply {
                    val itemString = prefs[QRCodeWidget.KEY_QR_CODE_ITEM]
                    Timber.d("Updating widget: $glanceId, data: $itemString")
                    itemString?.let {
                        val item = Json.decodeFromString<QRCodeItem>(itemString)
                        val updatedItem = qrCodesRepository.getQRCode(item.id)

                        Timber.d("Updating widget: $glanceId, new title: ${updatedItem?.name}")
                        this[QRCodeWidget.KEY_QR_CODE_ITEM] = Json.encodeToString(updatedItem)
                    }
                }
                widget.update(appContext, glanceId)
            }
        }
        return Result.success()
    }
}
