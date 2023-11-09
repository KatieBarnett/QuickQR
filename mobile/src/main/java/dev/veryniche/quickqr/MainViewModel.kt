package dev.veryniche.quickqr

import android.content.Context
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.quickqr.core.encodeImage
import dev.veryniche.quickqr.storage.QRCodesRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    qrCodesRepository: QRCodesRepository
) : ViewModel() {

    val tiles = qrCodesRepository.getQRCodes()

    val barcodeScannerOptions = GmsBarcodeScannerOptions.Builder()
        .enableAutoZoom() // available on 16.1.0 and higher
        .build()

    fun scanBarcode(context: Context) {
        val scanner = GmsBarcodeScanning.getClient(
            context,
            barcodeScannerOptions
        )
        scanner.startScan()
            .addOnSuccessListener { barcode ->
                // Task completed successfully
                Timber.d("got barcode ${barcode.displayValue}")
                val barcodeContent = barcode.displayValue
                val qrCodeBitmapBase64 = barcode.displayValue?.let {
                    val base64 = createQRImage(it)
                    Timber.d("got barcode base64 $base64")
                    base64
                }
            }
            .addOnCanceledListener {
                // Task canceled
                Timber.d("barcode cancelled")
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                Timber.e(e)
            }
    }

    fun createQRImage(content: String): String {
        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        val qrgEncoder = QRGEncoder("https://veryniche.dev/", null, QRGContents.Type.TEXT, 300)
//        val qrgEncoder = QRGEncoder(content, null, QRGContents.Type.TEXT, 300)
//        qrgEncoder.setColorBlack(Color.RED)
//        qrgEncoder.setColorWhite(Color.BLUE)∂
        // Getting QR-Code as Bitmap∂
        return qrgEncoder.getBitmap(0).encodeImage()
    }
}
