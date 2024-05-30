package dev.veryniche.quickqr

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.navigation.QuickQRNavHost
import dev.veryniche.quickqr.purchase.PurchaseManager
import dev.veryniche.quickqr.purchase.isProPurchased
import dev.veryniche.quickqr.purchase.purchasePro
import dev.veryniche.quickqr.update.AppUpdateHelper
import dev.veryniche.quickqr.util.BarcodeClientHelper
import dev.veryniche.quickqr.util.Settings
import timber.log.Timber

// At the top level of your kotlin file:
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Settings.DATA_STORE_KEY)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var appUpdateHelper: AppUpdateHelper

    private val updateLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        // handle callback
        if (result.data == null) {
            return@registerForActivityResult
        }

        if (result.resultCode != RESULT_OK) {
            Timber.e("Update flow failed! Result code: " + result.resultCode)
            // If the update is canceled or fails,
            // you can request to start the update again.
        } else {
            Timber.d("In app update succeeded")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        firebaseAnalytics = Firebase.analytics
        MobileAds.initialize(this) { initializationStatus ->
            Timber.d("AdMob init: ${initializationStatus.adapterStatusMap}")
        }
        setContent {
            val coroutineScope = rememberCoroutineScope()
            val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
            val purchaseManager = remember { PurchaseManager(this, coroutineScope) }
            LaunchedEffect(Unit) {
                purchaseManager.connectToBilling()
            }

            val purchasedProducts by purchaseManager.purchases.collectAsStateWithLifecycle(
                lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
            )
            var showPurchaseErrorMessage by rememberSaveable { mutableStateOf<Int?>(null) }
            var showBarcodeModuleErrorMessage by rememberSaveable { mutableStateOf<Int?>(null) }

            BarcodeClientHelper(this) { showBarcodeModuleErrorMessage = it }.checkInstallBarcodeModule()

            appUpdateHelper = AppUpdateHelper(this, updateLauncher, snackbarHostState, coroutineScope)
            appUpdateHelper.checkForUpdates()

            QuickQRThemeMobileApp(
                isProPurchased = isProPurchased(purchasedProducts),
                onProPurchaseClick = {
                    purchasePro(purchaseManager) {
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
            true, {},
            remember {
                SnackbarHostState()
            }
        )
    }
}
