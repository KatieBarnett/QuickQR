package dev.veryniche.quickqr.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import dev.veryniche.quickqr.components.ShowkaseActionIcon
import dev.veryniche.quickqr.components.TileGrid
import dev.veryniche.quickqr.core.model.QRCodeItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    openExpandedQRCode: (id: Int) -> Unit,
    modifier: Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showEditSheet by remember { mutableStateOf(false) }
    var editSheetContext by remember { mutableStateOf<QRCodeItem?>(null) }
    var showAddSheet by remember { mutableStateOf(false) }
    val viewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current
    val scannedCode by viewModel.scannedCode.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleLarge,
                ) },
                actions = {
                    if (BuildConfig.DEBUG) {
                        ShowkaseActionIcon()
                    }
                },
            )
        },
        modifier = modifier
    ) { contentPadding ->
        val tiles: List<QRCodeItem> by viewModel.tiles.collectAsStateWithLifecycle(listOf())
        Box(Modifier.padding(contentPadding)) {
            TileGrid(
                tiles = tiles,
                addTile = {
                    editSheetContext = null
                    showAddSheet = true
                },
                longPressDetail = {
                    editSheetContext = it
                    showEditSheet = true
                },
                longPressCode = {
                    openExpandedQRCode(it.id)
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
        if (showAddSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showAddSheet = false
                    scope.launch {
                        viewModel.scannedCode.emit(null)
                    }
                },
                sheetState = sheetState
            ) {
                AddSheet(
                    scannedCode = scannedCode,
                    onSaveClick = { name, content, icon, primaryColor ->
                        showAddSheet = false
                        viewModel.processEdit(name = name, content = content, icon = icon!!, primaryColor = primaryColor!!)
                    },
                    onScanClick = { viewModel.scanBarcode(context) },
//                    onCloseClick = {
//                        scope.launch { sheetState.hide() }.invokeOnCompletion {
//                            if (!sheetState.isVisible) {
//                                showAddSheet = false
//                            }
//                        }
//                    },
                    modifier = Modifier
                )
            }
        } else if (showEditSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showEditSheet = false
                },
                sheetState = sheetState
            ) {
                EditSheet(
                    initialItem = editSheetContext,
                    onSaveClick = { name, content, icon, primaryColor ->
                        showEditSheet = false
                        viewModel.processEdit(editSheetContext?.id ?: -1, name, content, icon, primaryColor)
                    },
                    scannedCode = scannedCode,
                    onScanClick = {
                        viewModel.scanBarcode(context)
                    },
//                    onColorClick = {},
//                    onIconClick = {},
//                    onCloseClick = {
//                        scope.launch { sheetState.hide() }.invokeOnCompletion {
//                            if (!sheetState.isVisible) {
//                                showEditSheet = false
//                            }
//                        }
//                    },
                )
            }
        }
    }
}
