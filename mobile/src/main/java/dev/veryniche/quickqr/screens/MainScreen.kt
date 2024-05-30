package dev.veryniche.quickqr.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.veryniche.quickqr.MainViewModel
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.analytics.Analytics
import dev.veryniche.quickqr.analytics.TrackedScreen
import dev.veryniche.quickqr.analytics.trackAction
import dev.veryniche.quickqr.analytics.trackMainScreenView
import dev.veryniche.quickqr.components.BannerAd
import dev.veryniche.quickqr.components.BannerAdLocation
import dev.veryniche.quickqr.components.TileGrid
import dev.veryniche.quickqr.components.TopAppBarTitle
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.purchase.isProVersionRequired
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    openExpandedQRCode: (id: Int) -> Unit,
    actionIcons: @Composable () -> Unit,
    isProPurchased: Boolean,
    onProPurchaseClick: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    modifier: Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var showEditSheet by rememberSaveable { mutableStateOf(false) }
    var editSheetContext by rememberSaveable { mutableStateOf<QRCodeItem?>(null) }
    var showAddSheet by rememberSaveable { mutableStateOf(false) }
    var showConfirmChanges by rememberSaveable { mutableStateOf(false) }
    var showPurchase by rememberSaveable { mutableStateOf(false) }
    var showBarcodeScanErrorMessage by rememberSaveable { mutableStateOf<Int?>(null) }
    val editSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            if (it == SheetValue.Hidden) {
                showConfirmChanges = true
                false
            } else {
                // We're expanding the sheet so we always return true
                true
            }
        }
    )
    val addSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            if (it == SheetValue.Hidden) {
                showConfirmChanges = true
                false
            } else {
                // We're expanding the sheet so we always return true
                true
            }
        }
    )
    val viewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current
    val scannedCode by viewModel.scannedCode.collectAsStateWithLifecycle(
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )
    var flipAllCardsToFront by rememberSaveable { mutableStateOf<Int?>(null) }

    val tiles: List<QRCodeItem> by viewModel.tiles.collectAsStateWithLifecycle(
        initialValue = listOf(),
        lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    )

    TrackedScreen {
        trackMainScreenView(tiles.size)
    }

    fun hideAddSheet() {
        coroutineScope.launch {
            addSheetState.hide()
        }.invokeOnCompletion {
            if (!addSheetState.isVisible) {
                showAddSheet = false
            }
        }
    }

    fun hideEditSheet() {
        coroutineScope.launch {
            editSheetState.hide()
        }.invokeOnCompletion {
            if (!editSheetState.isVisible) {
                showEditSheet = false
                editSheetContext = null
            }
        }
    }

    fun resetScreen() {
        showConfirmChanges = false
        hideAddSheet()
        hideEditSheet()
        coroutineScope.launch {
            viewModel.scannedCode.emit(null)
        }
    }

    BackHandler(enabled = addSheetState.isVisible || editSheetState.isVisible) {
        coroutineScope.launch {
            showConfirmChanges = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TopAppBarTitle(R.string.app_name)
                },
                actions = {
                    IconButton(
                        onClick = {
                            flipAllCardsToFront = (flipAllCardsToFront ?: 0) + 1
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = stringResource(id = R.string.navigate_flip_all_cards)
                        )
                    }
                    actionIcons.invoke()
                },
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier
    ) { contentPadding ->
        Column(Modifier.padding(contentPadding)) {
            TileGrid(
                tiles = tiles,
                addTile = {
                    editSheetContext = null

                    Timber.d(
                        "isProPurchased: $isProPurchased isProVersionRequired: ${isProVersionRequired(tiles.size)}"
                    )

                    if (isProPurchased || !isProVersionRequired(tiles.size)) {
                        showAddSheet = true
                    } else {
                        showPurchase = true
                    }
                    trackAction(Analytics.Action.AddCode, isProPurchased)
                },
                longPressDetail = {
                    editSheetContext = it
                    showEditSheet = true
                    trackAction(Analytics.Action.EditCode, isProPurchased)
                },
                longPressCode = {
                    openExpandedQRCode(it.id)
                    trackAction(Analytics.Action.ExpandCode, isProPurchased)
                },
                triggerShowAllFront = flipAllCardsToFront,
                modifier = Modifier.fillMaxWidth().weight(1f),
            )
            if (!isProPurchased) {
                BannerAd(location = BannerAdLocation.MainScreen)
            }
        }
        if (showAddSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showConfirmChanges = true
                },
                windowInsets = WindowInsets.ime,
                sheetState = addSheetState
            ) {
                AddSheet(
                    scannedCode = scannedCode,
                    onSaveClick = { name, content, icon, primaryColor ->
                        resetScreen()
                        viewModel.processEdit(
                            name = name,
                            content = content,
                            icon = icon!!,
                            primaryColor = primaryColor!!
                        )
                    },
                    onScanClick = {
                        viewModel.scanBarcode(context) {
                            showBarcodeScanErrorMessage = it
                        }
                    },
                    modifier = Modifier
                )
            }
        } else if (showEditSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showConfirmChanges = true
                },
                sheetState = editSheetState
            ) {
                EditSheet(
                    initialItem = editSheetContext,
                    onSaveClick = { name, content, icon, primaryColor ->
                        resetScreen()
                        viewModel.processEdit(
                            editSheetContext?.id ?: -1,
                            name,
                            content,
                            icon,
                            primaryColor
                        )
                    },
                    scannedCode = scannedCode,
                    onScanClick = {
                        viewModel.scanBarcode(context) { showBarcodeScanErrorMessage = it }
                        trackAction(Analytics.Action.ScanCodeEdit, isProPurchased)
                    },
                    onDeleteClick = {
                        viewModel.deleteCode(editSheetContext?.id ?: -1)
                        trackAction(Analytics.Action.DeleteCode, isProPurchased)
                        resetScreen()
                    }
                )
            }
        }
        if (showConfirmChanges) {
            AlertDialog(
                onDismissRequest = { showConfirmChanges = false },
                title = { Text(stringResource(R.string.confirm_changes_title)) },
                text = { Text(stringResource(R.string.confirm_changes_text)) },
                confirmButton = {
                    TextButton(onClick = {
                        resetScreen()
                    }) {
                        Text(stringResource(R.string.confirm_changes_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showConfirmChanges = false
                    }) {
                        Text(stringResource(R.string.confirm_changes_dismiss))
                    }
                },
                properties = DialogProperties(
                    dismissOnBackPress = false,
                    dismissOnClickOutside = false
                )
            )
        }
        if (showPurchase) {
            AlertDialog(
                onDismissRequest = { showPurchase = false },
                title = { Text(stringResource(R.string.pro_purchase_title)) },
                text = { Text(stringResource(R.string.pro_purchase_text)) },
                confirmButton = {
                    TextButton(onClick = {
                        trackAction(Analytics.Action.AddMoreProVersion)
                        onProPurchaseClick.invoke()
                        showPurchase = false
                    }) {
                        Text(stringResource(R.string.pro_purchase_confirm))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showPurchase = false
                    }) {
                        Text(stringResource(R.string.pro_purchase_dismiss))
                    }
                }
            )
        }
        showBarcodeScanErrorMessage?.let { message ->
            AlertDialog(
                onDismissRequest = { showBarcodeScanErrorMessage = null },
                title = { Text(stringResource(R.string.barcode_scan_error)) },
                text = { Text(stringResource(message)) },
                confirmButton = {
                    TextButton(onClick = {
                        showBarcodeScanErrorMessage = null
                    }) {
                        Text(stringResource(R.string.barcode_scan_error_dismiss))
                    }
                }
            )
        }
    }
}
