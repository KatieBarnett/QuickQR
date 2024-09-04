package dev.veryniche.quickqr.widgets

import android.app.WallpaperManager
import android.app.WallpaperManager.FLAG_SYSTEM
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItem
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItemLongText
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.model.QRColor
import dev.veryniche.quickqr.core.theme.Dimen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// Android entry point for DI https://stackoverflow.com/questions/75511282/display-data-from-database-room-in-widget-glance-using-jetpack-compose

class QRCodeWidget : GlanceAppWidget(errorUiLayout = R.layout.qrcode_widget_error_layout) {

    companion object {
        private val extraSmallMode = DpSize(120.dp, 120.dp)
        private val smallMode = DpSize(184.dp, 184.dp)
        private val mediumMode = DpSize(260.dp, 260.dp)
        private val largeMode = DpSize(300.dp, 300.dp)

        val KEY_QR_CODE_ITEM = stringPreferencesKey("qr_code_item")
        val KEY_COLORED_BACKGROUND = stringPreferencesKey("use_colored_background")
    }

    // Define the supported sizes for this widget.
    // The system will decide which one fits better based on the available space
    override val sizeMode: SizeMode = SizeMode.Responsive(
        setOf(extraSmallMode, smallMode, mediumMode, largeMode),
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // In this method, load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.

        provideContent {
            GlanceTheme(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    GlanceTheme.colors
                } else {
                    QuickQRGlanceColorScheme.colors
                }
            ) {
                val wallpaperManager = WallpaperManager.getInstance(context)
                val colors = wallpaperManager.getWallpaperColors(FLAG_SYSTEM)
                var useDarkColorOnWallpaper by remember {
                    mutableStateOf(
                        getUseDarkColorOnWallPaper(colors, FLAG_SYSTEM) ?: false
                    )
                }
                DisposableEffect(wallpaperManager) {
                    val listener = WallpaperManager.OnColorsChangedListener { colors, type ->
                        getUseDarkColorOnWallPaper(colors, type)?.let {
                            useDarkColorOnWallpaper = it
                        }
                    }
                    wallpaperManager.addOnColorsChangedListener(listener, Handler(Looper.getMainLooper()))
                    onDispose {
                        wallpaperManager.removeOnColorsChangedListener(listener)
                    }
                }
                Content(useDarkColorOnWallpaper)
            }
        }
    }

    @Composable
    private fun Content(useDarkText: Boolean) {
        val state = currentState(TileWidget.KEY_QR_CODE_ITEM)
        val item = try {
            state?.let {
                Json.decodeFromString<QRCodeItem>(state)
            }
        } catch (e: Exception) {
            null
        }
        if (item != null) {
            val useColorBackground = currentState(TileWidget.KEY_COLORED_BACKGROUND)?.toBoolean() ?: false
            QRCodeWidget(item, useColorBackground, useDarkText)
        } else {
            WidgetError(useDarkText)
        }
    }
}

@Composable
fun QRCodeWidget(
    qrCodeItem: QRCodeItem,
    useColorBackground: Boolean,
    useDarkTextOnBackground: Boolean,
    modifier: GlanceModifier = GlanceModifier
) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        val columnModifier = if (useColorBackground) {
            GlanceModifier
                .background(qrCodeItem.primaryColor.color)
                .cornerRadius(4.dp)
        } else {
            GlanceModifier
        }
        val context = LocalContext.current
        val intent = Intent(context, ExpandedCodeActivity::class.java)
        intent.putExtra(ExpandedCodeActivity.ARG_QR_CODE_ITEM, Json.encodeToString(qrCodeItem))
        Column(
            modifier = columnModifier
                .clickable(actionStartActivity(intent)),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                provider = ImageProvider(qrCodeItem.imageBitmap.asAndroidBitmap()),
                contentDescription = qrCodeItem.name,
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(
                        top = Dimen.spacingThreeQuarters,
                        start = Dimen.spacingThreeQuarters,
                        end = Dimen.spacingThreeQuarters
                    )
            )
            Text(
                text = qrCodeItem.name,
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    color =
                    if (useColorBackground) {
                        ColorProvider(qrCodeItem.secondaryColor)
                    } else {
                        if (useDarkTextOnBackground) {
                            ColorProvider(Color.Black)
                        } else {
                            ColorProvider(Color.White)
                        }
                    }
                ),
                modifier = GlanceModifier
                    .padding(horizontal = Dimen.spacing, vertical = Dimen.spacingQuarter)
            )
        }
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(heightDp = 120, widthDp = 120)
@Preview(heightDp = 180, widthDp = 120)
@Composable
fun QRCodeWidgetPreview() {
    GlanceTheme(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            GlanceTheme.colors
        } else {
            QuickQRGlanceColorScheme.colors
        }
    ) {
        Box(GlanceModifier.padding(Dimen.spacing)) {
            QRCodeWidget(sampleQRCodeItem.copy(primaryColor = QRColor.Primary), false, false)
        }
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(heightDp = 120, widthDp = 120)
@Preview(heightDp = 180, widthDp = 120)
@Composable
fun QRCodeWidgetWithBackgroundPreview() {
    GlanceTheme(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            GlanceTheme.colors
        } else {
            QuickQRGlanceColorScheme.colors
        }
    ) {
        Box(GlanceModifier.padding(Dimen.spacing).width(120.dp)) {
            QRCodeWidget(sampleQRCodeItem.copy(primaryColor = QRColor.Primary), true, false)
        }
    }
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(heightDp = 180, widthDp = 120)
@Composable
fun QRCodeWidgetLongTextPreview() {
    GlanceTheme(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            GlanceTheme.colors
        } else {
            QuickQRGlanceColorScheme.colors
        }
    ) {
        Box(GlanceModifier.padding(Dimen.spacing).width(120.dp)) {
            QRCodeWidget(sampleQRCodeItemLongText.copy(primaryColor = QRColor.Primary), true, false)
        }
    }
}
