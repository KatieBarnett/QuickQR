package dev.veryniche.quickqr.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import dev.veryniche.quickqr.ExpandedCodeViewModel
import dev.veryniche.quickqr.components.ExpandedQRCode
import dev.veryniche.quickqr.util.Analytics
import dev.veryniche.quickqr.util.TrackedScreen
import dev.veryniche.quickqr.util.trackScreenView

@Composable
fun ExpandedCodeScreen(id: Int?, modifier: Modifier) {
    val viewModel: ExpandedCodeViewModel = hiltViewModel()
    var qrCodeItem by remember { mutableStateOf(viewModel.getQRCodeItem(id)) }

    TrackedScreen {
        trackScreenView(Analytics.Screen.ExpandedCode)
    }

    qrCodeItem?.let {
        ExpandedQRCode(qrCodeItem = it, modifier.fillMaxSize())
    } ?: GenericErrorScreen(modifier)
}
