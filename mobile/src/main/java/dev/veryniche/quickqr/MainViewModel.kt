package dev.veryniche.quickqr

import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItem
import dev.veryniche.quickqr.core.encodeImage
import dev.veryniche.quickqr.core.model.QRCodeItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {

    private val _tiles = MutableStateFlow(listOf(
        sampleQRCodeItem,
        sampleQRCodeItem,
        sampleQRCodeItem,
        sampleQRCodeItem,
        sampleQRCodeItem,
        sampleQRCodeItem,
        sampleQRCodeItem,
        sampleQRCodeItem,
    ))
    val tiles: StateFlow<List<QRCodeItem>>
        get() = _tiles.asStateFlow()

    val barcodeScannerOptions = GmsBarcodeScannerOptions.Builder()
        .enableAutoZoom() // available on 16.1.0 and higher
        .build()

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