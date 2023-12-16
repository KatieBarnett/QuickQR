package dev.veryniche.quickqr.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItem
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.theme.Dimen
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.previews.PreviewScreen

@Composable
fun TileGrid(
    tiles: List<QRCodeItem>,
    addTile: () -> Unit,
    longPressDetail: (QRCodeItem) -> Unit,
    longPressCode: (QRCodeItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = Dimen.tileWidthDefault),
        contentPadding = PaddingValues(Dimen.qRDetailDisplaySpacing),
        horizontalArrangement = Arrangement.spacedBy(Dimen.qRDetailDisplaySpacing),
        verticalArrangement = Arrangement.spacedBy(Dimen.qRDetailDisplaySpacing),
        modifier = modifier
    ) {
        item {
            AddTile(addTile)
        }
        items(tiles) { tile ->
            Tile(tile, longPressDetail, longPressCode)
        }
    }
}

@PreviewScreen
@Composable
fun TileGridPreview() {
    QuickQRTheme {
        TileGrid(
            tiles = listOf(
                sampleQRCodeItem,
                sampleQRCodeItem,
                sampleQRCodeItem,
                sampleQRCodeItem,
                sampleQRCodeItem,
                sampleQRCodeItem,
                sampleQRCodeItem,
                sampleQRCodeItem,
            ),
            addTile = {},
            longPressDetail = {},
            longPressCode = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
