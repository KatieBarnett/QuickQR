package dev.veryniche.quickqr

import android.content.Context
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.quickqr.core.encodeImage
import dev.veryniche.quickqr.core.model.Icon
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.storage.QRCodesRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val qrCodesRepository: QRCodesRepository
) : ViewModel() {

    val tiles = qrCodesRepository.getQRCodes()

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
                    base64
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
        name: String?,
        content: String?,
        icon: Icon,
        primaryColor: Color,
        secondaryColor: Color
    ): ImageBitmap? {
        if (name == null) {
            // TODO validation
        } else if (content == null) {
            // TODO validation
        } else {
            val qrImageBase64 = createQRImage(content)
            val qrCodeItem = QRCodeItem(
                id = 1,
                name = name.trim(),
                content = content.trim(),
                imageBase64 = qrImageBase64,
                icon = icon,
                primaryColor = primaryColor,
                secondaryColor = secondaryColor,
                lastModified = Date()
            )
            viewModelScope.launch {
                saveQRCodeItem(qrCodeItem)
            }
            return qrCodeItem.imageBitmap
        }
        return null
    }

    private suspend fun saveQRCodeItem(item: QRCodeItem) {
        qrCodesRepository.saveQRCode(item)
    }

    fun createQRImage(content: String): String {
        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        val qrgEncoder = QRGEncoder("https://veryniche.dev/", null, QRGContents.Type.TEXT, 300)
//        val qrgEncoder = QRGEncoder(content, null, QRGContents.Type.TEXT, 300)
//        qrgEncoder.setColorBlack(Color.RED)
//        qrgEncoder.setColorWhite(Color.BLUE)
        // Getting QR-Code as Bitmap∂
        return qrgEncoder.getBitmap(0).encodeImage()
    }
}
