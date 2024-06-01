package dev.veryniche.quickqr.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import dev.veryniche.quickqr.ExpandedCodeViewModel
import dev.veryniche.quickqr.analytics.Analytics
import dev.veryniche.quickqr.analytics.TrackedScreen
import dev.veryniche.quickqr.analytics.trackAction
import dev.veryniche.quickqr.analytics.trackScreenView
import dev.veryniche.quickqr.components.ExpandedQRCode
import dev.veryniche.quickqr.util.resolveUrl

@Composable
fun ExpandedCodeScreen(id: Int?, modifier: Modifier) {
    val viewModel: ExpandedCodeViewModel = hiltViewModel()
    val qrCodeItem by remember { mutableStateOf(viewModel.getQRCodeItem(id)) }
    val context = LocalContext.current

    TrackedScreen {
        trackScreenView(Analytics.Screen.ExpandedCode)
    }

    qrCodeItem?.let {
        ExpandedQRCode(
            qrCodeItem = it,
            onVisitClick = {
                trackAction(Analytics.Action.VisitContentDestination)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.content.resolveUrl()))
                context.startActivity(intent)
            },
            modifier = modifier.fillMaxSize()
        )
    } ?: GenericErrorScreen(modifier)
}
