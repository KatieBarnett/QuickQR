package dev.veryniche.quickqr.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import dev.veryniche.quickqr.ExpandedCodeViewModel
import dev.veryniche.quickqr.analytics.Analytics
import dev.veryniche.quickqr.analytics.TrackedScreen
import dev.veryniche.quickqr.analytics.trackAction
import dev.veryniche.quickqr.analytics.trackScreenView
import dev.veryniche.quickqr.components.ExpandedQRCode
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.util.resolveUrl

@Composable
fun ExpandedCodeScreen(id: Int?, modifier: Modifier = Modifier) {
    val viewModel: ExpandedCodeViewModel = hiltViewModel()
    val qrCodeItem by remember { mutableStateOf(viewModel.getQRCodeItem(id)) }

    TrackedScreen {
        trackScreenView(Analytics.Screen.ExpandedCode)
    }

    qrCodeItem?.let {
        ExpandedCodeScreen(it, Modifier.fillMaxSize())
    } ?: GenericErrorScreen(modifier)
}

@Composable
fun ExpandedCodeScreen(item: QRCodeItem, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    ExpandedQRCode(
        qrCodeItem = item,
        onVisitClick = {
            trackAction(Analytics.Action.VisitContentDestination)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.content.resolveUrl()))
            context.startActivity(intent)
        },
        modifier = modifier
    )
}
