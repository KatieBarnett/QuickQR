package dev.veryniche.quickqr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.AndroidEntryPoint
import dev.veryniche.quickqr.components.TileGrid
import dev.veryniche.quickqr.core.decodeImage
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuickQRThemeMobileApp()
        }
    }

    @Composable
    fun QuickQRThemeMobileApp() {
        QuickQRTheme {
            val context = LocalContext.current
            val viewModel: MainViewModel = hiltViewModel()
            var barcodeContent by remember { mutableStateOf<String?>(null) }
            var qrCodeBitmapBase64 by remember { mutableStateOf<String?>(null) }
            val qrCodeBitmap by remember { derivedStateOf {
                qrCodeBitmapBase64?.decodeImage()
            } }

            val tiles: List<QRCodeItem> by viewModel.tiles.collectAsStateWithLifecycle(listOf())
            var cellsPerRow by remember { mutableStateOf(3) }
            Box(Modifier) {
                    TileGrid(
                        tiles = tiles,
                        cellsPerRow = cellsPerRow,
                        addTile = {
                            val scanner = GmsBarcodeScanning.getClient(
                                context,
                                viewModel.barcodeScannerOptions
                            )
                            scanner.startScan()
                                .addOnSuccessListener { barcode ->
                                    // Task completed successfully
                                    Timber.d("got barcode ${barcode.displayValue}")
                                    barcodeContent = barcode.displayValue
                                    qrCodeBitmapBase64 = barcode.displayValue?.let {
                                        val base64 = viewModel.createQRImage(it)
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
                        },
                        longPress = {},
                        modifier = Modifier.fillMaxSize(),
                    )
                }


//            Column {
//                Button(onClick = {
//                    val scanner = GmsBarcodeScanning.getClient(context, viewModel.barcodeScannerOptions)
//                    scanner.startScan()
//                        .addOnSuccessListener { barcode ->
//                            // Task completed successfully
//                            Timber.d("got barcode ${barcode.displayValue}")
//                            barcodeContent = barcode.displayValue
//                            qrCodeBitmapBase64 = barcode.displayValue?.let {
//                                val base64 = viewModel.createQRImage(it)
//                                Timber.d("got barcode base64 $base64")
//                                base64
//                            }
//                        }
//                        .addOnCanceledListener {
//                            // Task canceled
//                            Timber.d("barcode cancelled")
//                        }
//                        .addOnFailureListener { e ->
//                            // Task failed with an exception
//                            Timber.e(e)
//                        }
//
//                }) {
//                    Text("Scan barcode")
//                }
//                Text(barcodeContent.orEmpty(), color = MaterialTheme.colorScheme.primary)
//                qrCodeBitmap?.asImageBitmap()?.let {
//                    Image(it, barcodeContent.orEmpty())
//                }
//            }


//            var showWelcomeDialog by remember { mutableStateOf(true) }
//            WelcomeToFlipNavHost(navController = navController)
//            if (showWelcomeDialog) {∂
//                WelcomeDialog(navController) {
//                    showWelcomeDialog = false
//                }
//            }
        }
    }

    @Preview(group = "Full App", showSystemUi = true, showBackground = true)
    @Composable
    fun DefaultPreview() {
        QuickQRThemeMobileApp()
    }
}