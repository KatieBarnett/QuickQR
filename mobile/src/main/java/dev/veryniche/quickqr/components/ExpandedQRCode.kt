package dev.veryniche.quickqr.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.veryniche.quickqr.core.Constants
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.theme.Dimen
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.previews.ComponentPreviews
import dev.veryniche.quickqr.previews.ScreenPreview

@Composable
fun ExpandedQRCode(
    qrCodeItem: QRCodeItem,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.scrim)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                color = qrCodeItem.primaryColor,
                shape = RoundedCornerShape(8.dp)
            ) {
                Image(
                    imageVector = qrCodeItem.icon.vector,
                    colorFilter = ColorFilter.tint(qrCodeItem.secondaryColor),
                    contentDescription = qrCodeItem.content,
                    modifier = Modifier.fillMaxSize()
                )
            }
            ElevatedCard(
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.aspectRatio(1f)
            ) {
                Image(
                    bitmap = qrCodeItem.imageBitmap,
                    contentDescription = qrCodeItem.content,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(Dimen.qRCodeDisplayPadding)
                )
            }
            Text(
                text = qrCodeItem.name,
                color = Color.White,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
            )
            Text(
                text = qrCodeItem.content,
                color = Color.White,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
            )
        }
    }
}

@ComponentPreviews
@ScreenPreview
@Composable
fun ExpandedQRCodePreview() {
    QuickQRTheme {
        ExpandedQRCode(
            qrCodeItem = Constants.sampleQRCodeItem,
            modifier = Modifier
        )
    }
}
