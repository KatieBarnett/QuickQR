package dev.veryniche.quickqr.widgets.tile

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import dev.veryniche.quickqr.MainViewModel
import dev.veryniche.quickqr.PurchaseViewModel
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.purchase.PurchaseManager
import dev.veryniche.quickqr.widgets.SelectQRCode
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber


//https://stackoverflow.com/questions/77088363/android-jetpack-glance-1-0-0-problems-updating-widget


@AndroidEntryPoint
class TileWidgetConfigurationActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MobileAds.initialize(this) { initializationStatus ->
            Timber.d("AdMob init: ${initializationStatus.adapterStatusMap}")
        }

        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        // If this activity was started with an intent without an app widget ID, just finish.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        // If the user backs out of the activity before reaching the end, the system notifies the
        // app widget host that the configuration is canceled and the host doesn't add the widget
        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(Activity.RESULT_CANCELED, resultValue)

        setContent {
            val coroutineScope = rememberCoroutineScope()
            val purchaseManager = remember { PurchaseManager(this, coroutineScope) }
            val purchaseViewModel: PurchaseViewModel =
                hiltViewModel<PurchaseViewModel, PurchaseViewModel.PurchaseViewModelFactory> { factory ->
                    factory.create(purchaseManager)
                }
            val isProPurchased by purchaseViewModel.isProPurchased.collectAsStateWithLifecycle(false)

            QuickQRTheme {
                val viewModel: MainViewModel = hiltViewModel()
                val tiles: List<QRCodeItem> by viewModel.tiles.collectAsStateWithLifecycle(
                    initialValue = listOf(),
                )

                SelectQRCode(
                    tiles = tiles,
                    isProPurchased = isProPurchased,
                    setWidgetQRCode = { item, useColoredBackground ->
                        coroutineScope.launch {
                            setWidgetQRCode(
                                this@TileWidgetConfigurationActivity,
                                appWidgetId,
                                item,
                                useColoredBackground
                            )
                        }
                    }
                )
            }
        }
    }

    private suspend fun setWidgetQRCode(
        context: Context,
        appWidgetId: Int,
        item: QRCodeItem,
        useColoredBackground: Boolean,
    ) {
        val manager = GlanceAppWidgetManager(this)
        val glanceId = manager.getGlanceIdBy(appWidgetId)
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[TileWidget.KEY_QR_CODE_ITEM] = Json.encodeToString(item)
            prefs[TileWidget.KEY_COLORED_BACKGROUND] = useColoredBackground.toString()
        }
        TileWidget().update(this, glanceId)

        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}
