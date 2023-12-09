package dev.veryniche.quickqr.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.veryniche.quickqr.BuildConfig
import dev.veryniche.quickqr.MainViewModel
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.components.Edit
import dev.veryniche.quickqr.components.ShowkaseActionIcon
import dev.veryniche.quickqr.components.TileGrid
import dev.veryniche.quickqr.core.model.QRCodeItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showEditSheet by remember { mutableStateOf(false) }
    var editSheetContext by remember { mutableStateOf<QRCodeItem?>(null) }
    val viewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { stringResource(id = R.string.app_name) },
                actions = {
                    if (BuildConfig.DEBUG) {
                        ShowkaseActionIcon()
                    }
                },
            )
        },
    ) { contentPadding ->
        val tiles: List<QRCodeItem> by viewModel.tiles.collectAsStateWithLifecycle(listOf())
        Box(Modifier.padding(contentPadding)) {
            TileGrid(
                tiles = tiles,
                addTile = {
                    editSheetContext = null
                    showEditSheet = true
                },
                longPressDetail = {
                    editSheetContext = it
                    showEditSheet = true
                },
                longPressCode = {},
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (showEditSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showEditSheet = false
                },
                sheetState = sheetState
            ) {
                // TODO get saved barcode in state and pass to Add New content
                Edit(
                    initialItem = editSheetContext,
                    onSaveClick = { name, content, icon, primaryColor, secondaryColor ->
                        viewModel.processEdit(name, content, icon, primaryColor, secondaryColor)
                    },
                    onScanClick = { viewModel.scanBarcode(context) },
                    onCloseClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showEditSheet = false
                            }
                        }
                    },
                )
            }
        }
    }
}
