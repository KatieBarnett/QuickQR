package dev.veryniche.quickqr

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.quickqr.core.encodeImage
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {

    val barcodeScannerOptions = GmsBarcodeScannerOptions.Builder()
        .enableAutoZoom() // available on 16.1.0 and higher
        .build()

    fun createQRImage(content: String): String {
        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        // Initializing the QR Encoder with your value to be encoded, type you required and Dimension
        val qrgEncoder = QRGEncoder(content, null, QRGContents.Type.TEXT, 300)
//        qrgEncoder.setColorBlack(Color.RED)
//        qrgEncoder.setColorWhite(Color.BLUE)∂
            // Getting QR-Code as Bitmap∂
        return qrgEncoder.getBitmap(0).encodeImage()
    }
}