package dev.veryniche.quickqr.widgets

import android.content.Context
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.text.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.veryniche.quickqr.MainActivity
import dev.veryniche.quickqr.core.model.QRCodeItem

// Android entry point for DI https://stackoverflow.com/questions/75511282/display-data-from-database-room-in-widget-glance-using-jetpack-compose

class QRCodeWidget : GlanceAppWidget() {


    override val sizeMode: SizeMode = SizeMode.Single

    companion object {
        private val extraSmallMode = DpSize(120.dp, 120.dp)
        private val smallMode = DpSize(184.dp, 184.dp)
        private val mediumMode = DpSize(260.dp, 260.dp)
        private val largeMode = DpSize(300.dp, 300.dp)
    }

//    // Define the supported sizes for this widget.
//    // The system will decide which one fits better based on the available space
//    override val sizeMode: SizeMode = SizeMode.Responsive(
//        setOf(extraSmallMode, smallMode, mediumMode, largeMode),
//    )

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
                Content()
            }
        }
    }

    @Composable
    private fun Content(
//        viewmodel: WidgetViewModel = hiltViewModel()
    ) {
//        val selectedQRCode by viewmodel.selectedQRCode.collectAsStateWithLifecycle(initialValue = null)
//        val qrCodes by viewmodel.availableQRCodes.collectAsStateWithLifecycle(initialValue = null)

//        if (qrCodes.isNullOrEmpty()) {
            OpenApp()
//        } else {
//            selectedQRCode?.let {
//                QRCodeDisplay(it)
//            } ?: QRCodeSelector(qrCodes.orEmpty())
//        }
    }

    @Composable
    private fun OpenApp(modifier: GlanceModifier = GlanceModifier) {
        Box(contentAlignment = Alignment.Center, modifier = modifier.fillMaxSize()) {
            Column(
                modifier = GlanceModifier
                    .background(GlanceTheme.colors.background),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "You have no QR Codes available, open the app to add one",
                    modifier = GlanceModifier.padding(12.dp)
                )

                Button(
                    text = "Open App",
                    onClick = actionStartActivity<MainActivity>()
                )
            }
        }
    }

    @Composable
    private fun QRCodeSelector(qrCodes: List<QRCodeItem>, modifier: GlanceModifier = GlanceModifier) {
        LazyColumn(modifier) {
            items(qrCodes) {
                Text(
                    text = it.name,
                    modifier = GlanceModifier.fillMaxWidth()
                )
            }
        }
    }

    @Composable
    private fun QRCodeDisplay(qrCodeItem: QRCodeItem, modifier: GlanceModifier = GlanceModifier) {
        Column(
            modifier = modifier.fillMaxSize()
                .background(GlanceTheme.colors.background),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = qrCodeItem.name, modifier = GlanceModifier.padding(12.dp))
        }
    }
}
