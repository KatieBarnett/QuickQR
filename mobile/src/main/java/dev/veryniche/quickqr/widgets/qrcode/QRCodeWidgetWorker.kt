package dev.veryniche.quickqr.widgets.qrcode

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

@HiltWorker
class QRCodeWidgetWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val qrCodesRepository: QRCodesRepository,
) : CoroutineWorker(appContext, workerParams) {

//    fun encodeIcon(icon: ImageVector) {
//        val drawable = icon.asDrawable(appContext)
//        val bitmap = Bitmap.createBitmap(
//            drawable.intrinsicWidth,
//            drawable.intrinsicHeight,
//            Bitmap.Config.ARGB_8888
//        )
//        val canvas = Canvas(bitmap)
//        drawable.setBounds(0, 0, canvas.width, canvas.height)
//        drawable.draw(canvas)
//
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
//        val byteArray = byteArrayOutputStream.toByteArray()
//
//        Base64.encodeToString(byteArray, Base64.DEFAULT)
//        return byteArray
//    }

    override suspend fun doWork(): Result {
        val manager = GlanceAppWidgetManager(appContext)
        val widget = QRCodeWidget()
        val glanceIds = manager.getGlanceIds(widget.javaClass)
        glanceIds.forEach { glanceId ->
            updateAppWidgetState(
                context = appContext,
                glanceId = glanceId,
            ) { prefs ->
                prefs.apply {
                    val itemString = prefs[QRCodeWidget.KEY_QR_CODE_ITEM]
                    itemString?.let {
                        val item = Json.decodeFromString<QRCodeItem>(itemString)
                        val updatedItem = qrCodesRepository.getQRCode(item.id)
                        this[QRCodeWidget.KEY_QR_CODE_ITEM] = Json.encodeToString(updatedItem)
                    }
                }
                widget.update(appContext, glanceId)
            }
        }
        return Result.success()
    }
}
