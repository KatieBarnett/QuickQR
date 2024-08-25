package dev.veryniche.quickqr.widgets

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import dev.veryniche.quickqr.PurchaseViewModel
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.analytics.Analytics
import dev.veryniche.quickqr.analytics.TrackedScreen
import dev.veryniche.quickqr.analytics.trackScreenView
import dev.veryniche.quickqr.components.BannerAd
import dev.veryniche.quickqr.components.BannerAdLocation
import dev.veryniche.quickqr.components.TopAppBarTitle
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItem
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.purchase.PurchaseManager
import dev.veryniche.quickqr.screens.ExpandedCodeScreen
import kotlinx.serialization.json.Json
import timber.log.Timber

@AndroidEntryPoint
class ExpandedCodeActivity : ComponentActivity() {

    companion object {
        const val ARG_QR_CODE_ITEM = "arg_qr_code_item"
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
            val purchaseManager = remember { PurchaseManager(this, coroutineScope) }
            val purchaseViewModel: PurchaseViewModel =
                hiltViewModel<PurchaseViewModel, PurchaseViewModel.PurchaseViewModelFactory> { factory ->
                    factory.create(purchaseManager)
                }
            val isProPurchased by purchaseViewModel.isProPurchased.collectAsStateWithLifecycle(false)
            var qrCodeItem: QRCodeItem? = null

            try {
                qrCodeItem = intent?.extras?.getString(ARG_QR_CODE_ITEM)?.let {
                    Json.decodeFromString<QRCodeItem>(it)
                } ?: throw IllegalArgumentException()
            } catch (e: Exception) {
                Timber.e(e)
                finish()
            }

            QuickQRTheme {
                qrCodeItem?.let {
                    ExpandedCodeActivityDisplay(
                        item = qrCodeItem,
                        isProPurchased = isProPurchased,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandedCodeActivityDisplay(
    item: QRCodeItem,
    isProPurchased: Boolean = false,
    modifier: Modifier = Modifier
) {
    TrackedScreen {
        trackScreenView(Analytics.Screen.ExpandedCodeWidget)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TopAppBarTitle(R.string.app_name)
                },
            )
        },
        modifier = modifier
    ) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            ExpandedCodeScreen(item)
            if (!isProPurchased) {
                BannerAd(location = BannerAdLocation.MainScreen)
            }
        }
    }
}

@Preview(group = "Full App", showSystemUi = true, showBackground = true)
@Composable
fun ExpandedCodeActivityDisplayPreview() {
    ExpandedCodeActivityDisplay(sampleQRCodeItem)
}
