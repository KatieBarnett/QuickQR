package dev.veryniche.quickqr.storage

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SavedGamesRepository @Inject constructor(
    private val qrCodesDataSource: QRCodesDataSource
) {

//    fun getSavedGames(): Flow<List<QRCodeItem>> {
//        return qrCodesDataSource.qRCodesFlow
//    }

//    suspend fun saveGame(qrCodeItem: QRCodeItem) {
//        qrCodesDataSource.saveQrCode(qrCodeItem.tp)
//    }
//
//    suspend fun deleteQRCodes(id: Int) {
//        qrCodesDataSource.deleteQRCode(id)
//    }
//
//    suspend fun deleteAllQRCodes() {
//        qrCodesDataSource.clearQRCodes()
//    }
}
