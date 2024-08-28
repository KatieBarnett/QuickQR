package dev.veryniche.quickqr

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.navigation.QuickQRNavHost
import dev.veryniche.quickqr.purchase.Products
import dev.veryniche.quickqr.purchase.PurchaseManager
import dev.veryniche.quickqr.update.AppUpdateHelper
import dev.veryniche.quickqr.util.BarcodeClientHelper
import dev.veryniche.quickqr.widgets.QRCodeWidgetWorker
import dev.veryniche.quickqr.widgets.TileWidgetWorker
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var appUpdateHelper: AppUpdateHelper

    @Deprecated(
        "This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}."
    )
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppUpdateHelper.REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                Timber.e("Update flow failed! Result code: $resultCode")
                // If the update is canceled or fails,
                // you can request to start the update again.
            } else {
                Timber.d("In app update succeeded")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MobileAds.initialize(this) { initializationStatus ->
            Timber.d("AdMob init: ${initializationStatus.adapterStatusMap}")
        }
        setContent {
            val coroutineScope = rememberCoroutineScope()
            val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }

            val purchaseManager = remember { PurchaseManager(this, coroutineScope) }

            val purchaseViewModel: PurchaseViewModel =
                hiltViewModel<PurchaseViewModel, PurchaseViewModel.PurchaseViewModelFactory> { factory ->
                    factory.create(purchaseManager)
                }
            val isProPurchased by purchaseViewModel.isProPurchased.collectAsStateWithLifecycle(false)

            var showPurchaseErrorMessage by rememberSaveable { mutableStateOf<Int?>(null) }

            var showBarcodeModuleErrorMessage by rememberSaveable { mutableStateOf<Int?>(null) }

            SideEffect {
                val qRCodeWidgetWorker = OneTimeWorkRequestBuilder<QRCodeWidgetWorker>().build()
                WorkManager.getInstance(this).enqueue(qRCodeWidgetWorker)
                val tileWidgetWorker = OneTimeWorkRequestBuilder<TileWidgetWorker>().build()
                WorkManager.getInstance(this).enqueue(tileWidgetWorker)
            }

            BarcodeClientHelper(this) { showBarcodeModuleErrorMessage = it }.checkInstallBarcodeModule()

            appUpdateHelper = AppUpdateHelper(this, snackbarHostState, coroutineScope)
            appUpdateHelper.checkForUpdates()

            QuickQRThemeMobileApp(
                isProPurchased = isProPurchased,
                onProPurchaseClick = {
                    purchaseViewModel.purchaseProduct(Products.proVersion) {
                        showPurchaseErrorMessage = it
                    }
                },
                snackbarHostState = snackbarHostState
            )

            QuickQRTheme {
                showPurchaseErrorMessage?.let { message ->
                    AlertDialog(
                        onDismissRequest = { showPurchaseErrorMessage = null },
                        title = { Text(stringResource(R.string.pro_purchase_title)) },
                        text = { Text(stringResource(message)) },
                        confirmButton = {
                            TextButton(onClick = {
                                showPurchaseErrorMessage = null
                            }) {
                                Text(stringResource(R.string.purchase_error_dismiss))
                            }
                        }
                    )
                }
                showBarcodeModuleErrorMessage?.let { message ->
                    AlertDialog(
                        onDismissRequest = { showBarcodeModuleErrorMessage = null },
                        title = { Text(stringResource(R.string.module_download)) },
                        text = { Text(stringResource(message)) },
                        confirmButton = {
                            TextButton(onClick = {
                                showBarcodeModuleErrorMessage = null
                            }) {
                                Text(stringResource(R.string.module_download_dismiss))
                            }
                        }
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::appUpdateHelper.isInitialized) {
            appUpdateHelper.checkUpdateStatus()
        }
    }

    @Composable
    fun QuickQRThemeMobileApp(
        isProPurchased: Boolean,
        onProPurchaseClick: () -> Unit,
        snackbarHostState: SnackbarHostState
    ) {
        QuickQRTheme {
            val navController = rememberNavController()
            QuickQRNavHost(
                navController = navController,
                isProPurchased = isProPurchased,
                onProPurchaseClick = onProPurchaseClick,
                snackbarHostState = snackbarHostState
            )
        }
    }

    @Preview(group = "Full App", showSystemUi = true, showBackground = true)
    @Composable
    fun DefaultPreview() {
        QuickQRThemeMobileApp(
            true,
            {},
            remember {
                SnackbarHostState()
            }
        )
    }
}
