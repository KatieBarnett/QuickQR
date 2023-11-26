package dev.veryniche.quickqr.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItem
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.theme.QuickQRTheme

@Composable
fun TileGrid(
    tiles: List<QRCodeItem>,
    cellsPerRow: Int,
    addTile: () -> Unit,
    longPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(cellsPerRow)
    ) {
        item {
            AddTile(addTile)
        }
        items(tiles) { tile ->
            Tile(tile)
        }
    }
}

@Preview
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
            cellsPerRow = 3,
            addTile = {},
            longPress = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
