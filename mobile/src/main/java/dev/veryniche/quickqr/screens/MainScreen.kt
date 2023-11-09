package dev.veryniche.quickqr.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.veryniche.quickqr.MainViewModel
import dev.veryniche.quickqr.components.AddNew
import dev.veryniche.quickqr.components.TileGrid
import dev.veryniche.quickqr.core.model.QRCodeItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val viewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current
    Scaffold { contentPadding ->
        val tiles: List<QRCodeItem> by viewModel.tiles.collectAsStateWithLifecycle(listOf())
        // TODO allow this to be configurable
        val cellsPerRow by remember { mutableStateOf(3) }
        Box(Modifier.padding(contentPadding)) {
            TileGrid(
                tiles = tiles,
                cellsPerRow = cellsPerRow,
                addTile = {
                    showBottomSheet = true
                },
                longPress = {},
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                // TODO get saved barcode in state and pass to Add New content
                AddNew(
                    onSaveClick = { },
                    onScanClick = { viewModel.scanBarcode(context) },
                    onCloseClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet = false
                            }
                        }
                    },
                )
            }
        }
    }
}
