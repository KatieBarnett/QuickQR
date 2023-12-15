package dev.veryniche.quickqr.storage

import androidx.datastore.core.DataStore
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.storage.models.Qrcodes
import dev.veryniche.quickqr.storage.util.toQRCode
import dev.veryniche.quickqr.storage.util.toQRCodeItem
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class QRCodesDataSource @Inject constructor(
    private val qRCodeDataStore: DataStore<Qrcodes>
) {

    companion object {
        internal const val PROTO_FILE_NAME = "qrcodes.pb"
    }

    val qRCodesFlow = qRCodeDataStore.data
        .map {
            it.qrCodesList.map { it.toQRCodeItem() }.sortedBy { it.sortOrder }
        }

    suspend fun saveQRCode(qrCode: QRCodeItem) {
        qRCodeDataStore.updateData { currentQRCodes ->
            val currentIndex = currentQRCodes.qrCodesList.indexOfFirst { it.id == qrCode.id }
            if (currentIndex != -1) {
                currentQRCodes.toBuilder().setQrCodes(currentIndex, qrCode.toQRCode()).build()
            } else {
                currentQRCodes.toBuilder().addQrCodes(qrCode.toQRCode(currentQRCodes.qrCodesList.size)).build()
            }
        }
    }

    fun getQRCode(id: Int): QRCodeItem? {
        return runBlocking {
            qRCodeDataStore.data.firstOrNull()
        }?.qrCodesList?.firstOrNull { it.id == id }?.toQRCodeItem()
    }

    suspend fun deleteQRCode(id: Int) {
        qRCodeDataStore.updateData { currentQRCodes ->
            val currentIndex = currentQRCodes.qrCodesList.indexOfFirst { it.id == id }
            if (currentIndex != -1) {
                currentQRCodes.toBuilder().removeQrCodes(currentIndex).build()
            } else {
                // Do nothing
                currentQRCodes.toBuilder().build()
            }
        }
    }

    suspend fun clearQRCodes() {
        qRCodeDataStore.updateData { currentQRCodes ->
            currentQRCodes.toBuilder().clearQrCodes().build()
        }
    }
}
