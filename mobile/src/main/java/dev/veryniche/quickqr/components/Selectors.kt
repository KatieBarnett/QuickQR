package dev.veryniche.quickqr.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.veryniche.quickqr.core.model.QRColor
import dev.veryniche.quickqr.core.model.QRIcon
import dev.veryniche.quickqr.core.theme.QuickQRTheme

@Composable
fun IconSelector(
    columns: Int,
    onIconClick: (QRIcon) -> Unit,
    modifier: Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(QRIcon.entries.filter { it.showInList }.toTypedArray()) { icon ->
            Image(
                imageVector = icon.vector,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                contentDescription = icon.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clickable {
                        onIconClick.invoke(icon)
                    }
            )
        }
    }
}

@Composable
fun ColorSelector(
    columns: Int,
    onColorClick: (QRColor) -> Unit,
    modifier: Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(QRColor.entries.filter { it.showInList }.toTypedArray()) { colorItem ->
            QRColorShape(
                colorItem,
                false,
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        onColorClick.invoke(colorItem)
                    }
            )
        }
    }
}

@Preview
@Composable
fun IconSelectorPreview() {
    QuickQRTheme {
        Surface {
            IconSelector(7, {}, modifier = Modifier)
        }
    }
}

@Preview
@Composable
fun ColorSelectorPreview() {
    QuickQRTheme {
        Surface {
            ColorSelector(7, {}, modifier = Modifier)
        }
    }
}
