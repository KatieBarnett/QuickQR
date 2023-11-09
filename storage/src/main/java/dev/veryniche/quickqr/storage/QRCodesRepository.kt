package dev.veryniche.quickqr.storage

import dev.veryniche.quickqr.core.model.QRCodeItem
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QRCodesRepository @Inject constructor(
    private val qrCodesDataSource: QRCodesDataSource
) {

    fun getQRCodes(): Flow<List<QRCodeItem>> {
        return qrCodesDataSource.qRCodesFlow
    }

    suspend fun saveQRCode(qrCodeItem: QRCodeItem) {
        qrCodesDataSource.saveQRCode(qrCodeItem)
    }

    suspend fun deleteQRCode(id: Int) {
        qrCodesDataSource.deleteQRCode(id)
    }

    suspend fun deleteAllQRCodes() {
        qrCodesDataSource.clearQRCodes()
    }
}
