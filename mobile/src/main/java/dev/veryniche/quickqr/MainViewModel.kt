package dev.veryniche.quickqr

import android.content.Context
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.quickqr.core.encodeImage
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.model.QRColor
import dev.veryniche.quickqr.core.model.QRIcon
import dev.veryniche.quickqr.storage.QRCodesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val qrCodesRepository: QRCodesRepository
) : ViewModel() {

    val tiles = qrCodesRepository.getQRCodes()
    val scannedCode = MutableStateFlow<ScannedCode?>(null)

    private val barcodeScannerOptions = GmsBarcodeScannerOptions.Builder()
        .enableAutoZoom() // available on 16.1.0 and higher
        .build()

    fun scanBarcode(context: Context) {
        val scanner = GmsBarcodeScanning.getClient(
            context,
            barcodeScannerOptions
        )
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                Timber.d("got barcode ${barcode.displayValue}")
                barcode.displayValue?.let {
                    val base64 = createQRImage(it)
                    Timber.d("got barcode base64 $base64")
                    viewModelScope.launch {
                        scannedCode.emit(ScannedCode(base64, it))
                    }
                }
            }
            .addOnCanceledListener {
                Timber.d("barcode cancelled")
            }
            .addOnFailureListener { e ->
                Timber.e(e)
            }
    }

    fun processEdit(
        id: Int = -1,
        name: String?,
        content: String?,
        icon: QRIcon,
        primaryColor: QRColor,
    ): ImageBitmap? {
        if (name == null) {
            return null // Shouldn't happen
        } else if (content == null) {
            return null // Shouldn't happen
        } else {
            val qrImageBase64 = createQRImage(content)
            val qrCodeItem = QRCodeItem(
                id = id,
                name = name.trim(),
                content = content.trim(),
                imageBase64 = qrImageBase64,
                icon = icon,
                primaryColor = primaryColor,
                lastModified = Date()
            )
            viewModelScope.launch {
                scannedCode.emit(null)
                saveQRCodeItem(qrCodeItem)
            }
            return qrCodeItem.imageBitmap
        }
    }

    fun deleteCode(id: Int) {
        viewModelScope.launch {
            qrCodesRepository.deleteQRCode(id)
        }
    }

    private suspend fun saveQRCodeItem(item: QRCodeItem) {
        qrCodesRepository.saveQRCode(item)
    }

    fun createQRImage(content: String): String {
        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        val qrgEncoder = QRGEncoder(content, null, QRGContents.Type.TEXT, 300)
        // TODO enable colour selection?
//        qrgEncoder.setColorBlack(Color.RED)
//        qrgEncoder.setColorWhite(Color.BLUE)
        // Getting QR-Code as Bitmap
        return qrgEncoder.getBitmap(0).encodeImage()
    }
}

data class ScannedCode(
    val base64: String,
    val content: String
)
