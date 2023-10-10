package dev.veryniche.quickqr

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.hilt.android.AndroidEntryPoint
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
            val navController = rememberNavController()
            val viewModel: MainViewModel = hiltViewModel()

            Button(onClick = {
                val scanner = GmsBarcodeScanning.getClient(this, viewModel.barcodeScannerOptions)
                scanner.startScan()
                    .addOnSuccessListener { barcode ->
                        // Task completed successfully
                        Timber.d("got barcode ${barcode.displayValue}")
                    }
                    .addOnCanceledListener {
                        // Task canceled
                        Timber.d("barcode cancelled")
                    }
                    .addOnFailureListener { e ->
                        // Task failed with an exception
                        Timber.e(e)
                    }

            }) {
                Text("Scan barcode")
            }


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