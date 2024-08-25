package dev.veryniche.quickqr.widgets

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint
import dev.veryniche.quickqr.MainActivity
import dev.veryniche.quickqr.MainViewModel
import dev.veryniche.quickqr.PurchaseViewModel
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.components.BannerAd
import dev.veryniche.quickqr.components.BannerAdLocation
import dev.veryniche.quickqr.components.Tile
import dev.veryniche.quickqr.components.TopAppBarTitle
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItem
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.theme.Dimen
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.purchase.PurchaseManager
import dev.veryniche.quickqr.update.AppUpdateHelper
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

@AndroidEntryPoint
class WidgetConfigurationActivity : ComponentActivity() {

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
                                this@WidgetConfigurationActivity,
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

    suspend fun setWidgetQRCode(context: Context, appWidgetId: Int, item: QRCodeItem, useColoredBackground: Boolean) {
        val manager = GlanceAppWidgetManager(this)
        val glanceId = manager.getGlanceIdBy(appWidgetId)
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[QRCodeWidget.KEY_QR_CODE_ITEM] = Json.encodeToString(item)
            prefs[QRCodeWidget.KEY_COLORED_BACKGROUND] = useColoredBackground.toString()
        }
        QRCodeWidget().update(this, glanceId)

        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectQRCode(
    tiles: List<QRCodeItem>,
    setWidgetQRCode: (QRCodeItem, Boolean) -> Unit,
    isProPurchased: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
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
        var useColoredBackground by remember { mutableStateOf(false) }
        Column(Modifier.padding(contentPadding)) {
            Text(
                text = stringResource(R.string.widget_configuration),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(Dimen.spacing)
            )
            Row(
                Modifier.padding(Dimen.spacing),
                horizontalArrangement = Arrangement.spacedBy(Dimen.spacing),
            ) {
                Text(
                    stringResource(R.string.widget_configuration_background),
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = useColoredBackground,
                    onCheckedChange = {
                        useColoredBackground = !useColoredBackground
                    }
                )
            }
            if (tiles.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = Dimen.tileWidthDefault),
                    contentPadding = PaddingValues(Dimen.qRDetailDisplaySpacing),
                    horizontalArrangement = Arrangement.spacedBy(Dimen.qRDetailDisplaySpacing),
                    verticalArrangement = Arrangement.spacedBy(Dimen.qRDetailDisplaySpacing),
                    modifier = modifier.weight(1f)
                ) {
                    items(tiles) { tile ->
                        Tile(
                            qrCodeItem = tile,
                            onClickOverride = {
                                setWidgetQRCode.invoke(tile, useColoredBackground)
                            },
                            longPressDetail = null,
                            longPressCode = null,
                        )
                    }
                    item {
                        Spacer(
                            Modifier.windowInsetsBottomHeight(
                                WindowInsets.systemBars
                            )
                        )
                    }
                }
            } else {
                Text(
                    text = stringResource(R.string.widget_configuration_empty),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(Dimen.spacing)
                )
                Button(onClick = {
                    startActivity(context, Intent(context, MainActivity::class.java), null)
                }) {
                    Text(text = stringResource(R.string.widget_configuration_empty_button))
                }
            }
            if (!isProPurchased) {
                BannerAd(location = BannerAdLocation.MainScreen)
            }
        }
    }
}

@Preview(group = "Full App", showSystemUi = true, showBackground = true)
@Composable
fun SelectQRCodePreview() {
    SelectQRCode(
        listOf(
            sampleQRCodeItem,
            sampleQRCodeItem,
            sampleQRCodeItem,
            sampleQRCodeItem,
            sampleQRCodeItem,
            sampleQRCodeItem,
            sampleQRCodeItem,
            sampleQRCodeItem,
        ),
        { _, _ -> }
    )
}

@Preview(group = "Full App", showSystemUi = true, showBackground = true)
@Composable
fun SelectQRCodeEmptyPreview() {
    SelectQRCode(
        listOf(),
        { _, _ -> }
    )
}
