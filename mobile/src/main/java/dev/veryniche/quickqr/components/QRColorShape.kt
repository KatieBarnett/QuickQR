package dev.veryniche.quickqr.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.veryniche.quickqr.core.model.QRColor
import dev.veryniche.quickqr.core.theme.Dimen
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.core.util.conditional

@Composable
fun QRColorShape(colorItem: QRColor, selected: Boolean = false, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .conditional(selected, {
                border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                    .padding(1.dp)
            })
            .clip(CircleShape)
            .background(colorItem.color)
    )
}

@Preview
@Composable
fun QRColorShapePreview() {
    QuickQRTheme {
        Surface {
            QRColorShape(
                QRColor.Violet,
                selected = false,
                modifier = Modifier
                    .size(Dimen.iconWidthDefault)
            )
        }
    }
}

@Preview
@Composable
fun QRColorShapeSelectedPreview() {
    QuickQRTheme {
        Surface {
            QRColorShape(
                QRColor.Violet,
                selected = true,
                modifier = Modifier
                    .size(Dimen.iconWidthDefault)
            )
        }
    }
}
