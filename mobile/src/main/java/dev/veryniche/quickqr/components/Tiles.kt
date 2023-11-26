package dev.veryniche.quickqr.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItem
import dev.veryniche.quickqr.core.theme.QuickQRTheme

@Preview
@Composable
fun TileFrontPreview() {
    QuickQRTheme {
        SideDetails(
            name = sampleQRCodeItem.name,
            content = sampleQRCodeItem.content,
            icon = sampleQRCodeItem.icon.vector,
            background = sampleQRCodeItem.primaryColor,
            contentColor = sampleQRCodeItem.secondaryColor,
            modifier = Modifier.aspectRatio(1f)
        )
    }
}




