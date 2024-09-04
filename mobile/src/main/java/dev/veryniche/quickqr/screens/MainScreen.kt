package dev.veryniche.quickqr.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
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
import dev.veryniche.quickqr.components.WelcomeDialog
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.purchase.isProVersionRequired
import dev.veryniche.quickqr.util.getActivity
import dev.veryniche.quickqr.widgets.qrcode.QRCodeWidgetWorker
import dev.veryniche.quickqr.widgets.tile.TileWidgetWorker
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
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
    var showPurchase by rememberSaveable { mutableStateOf(false) }
    var showBarcodeScanErrorMessage by rememberSaveable { mutableStateOf<Int?>(null) }
    var showEditSheet by rememberSaveable { mutableStateOf(false) }
    var editSheetContext by rememberSaveable { mutableStateOf<QRCodeItem?>(null) }
    var showAddSheet by rememberSaveable { mutableStateOf(false) }
    var showConfirmChanges by rememberSaveable { mutableStateOf(false) }
    var haveSheetChangesBeenMade by remember { mutableStateOf(false) }
    val editSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = {
            if (it == SheetValue.Hidden) {
                showConfirmChanges = haveSheetChangesBeenMade
                !haveSheetChangesBeenMade
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
                showConfirmChanges = haveSheetChangesBeenMade
                !haveSheetChangesBeenMade
            } else {
                // We're expanding the sheet so we always return true
                true
            }
        }
    )
    val viewModel: MainViewModel = hiltViewModel()
    val context = LocalContext.current
    val scannedCode by viewModel.scannedCode.collectAsStateWithLifecycle()
    var flipAllCardsToFront by rememberSaveable { mutableStateOf<Int?>(null) }

    val tiles: List<QRCodeItem> by viewModel.tiles.collectAsStateWithLifecycle(
        initialValue = listOf(),
    )

    val windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    val showWelcomeDialogOnStart by viewModel.showWelcomeDialog.collectAsStateWithLifecycle(null)
    var showWelcomeDialog by rememberSaveable(showWelcomeDialogOnStart) {
        mutableStateOf(showWelcomeDialogOnStart)
    }

    TrackedScreen {
        trackMainScreenView(tiles.size)
    }

    LifecycleEventEffect(
        event = Lifecycle.Event.ON_CREATE,
    ) {
        context.getActivity()?.let {
            viewModel.requestReviewIfAble(it)
            viewModel.incrementNumberOfOpens()
        }
    }

    fun hideAddSheet() {
        if (addSheetState.isVisible) {
            coroutineScope.launch {
                addSheetState.hide()
            }.invokeOnCompletion {
                if (!addSheetState.isVisible) {
                    showAddSheet = false
                }
            }
        }
    }

    fun hideEditSheet() {
        if (editSheetState.isVisible) {
            coroutineScope.launch {
                editSheetState.hide()
            }.invokeOnCompletion {
                if (!editSheetState.isVisible) {
                    showEditSheet = false
                    editSheetContext = null
                }
            }
        }
    }

    fun resetScreen() {
        showConfirmChanges = false
        haveSheetChangesBeenMade = false
        hideAddSheet()
        hideEditSheet()
        coroutineScope.launch {
            viewModel.scannedCode.emit(null)
        }
    }

    BackHandler(enabled = addSheetState.isVisible || editSheetState.isVisible) {
        resetScreen()
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
                    showAddSheet = false
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
                    onChangeMade = {
                        haveSheetChangesBeenMade = true
                    },
                    modifier = Modifier
                )
            }
        } else if (showEditSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showEditSheet = false
                },
                windowInsets = WindowInsets.ime,
                sheetState = editSheetState
            ) {
                EditSheet(
                    initialItem = editSheetContext,
                    onSaveClick = { name, content, icon, primaryColor ->
                        resetScreen()
                        val bitmap = viewModel.processEdit(
                            id = editSheetContext?.id ?: -1,
                            name = name,
                            content = content,
                            icon = icon,
                            primaryColor = primaryColor
                        )
                        val qRCodeWidgetWorker = OneTimeWorkRequestBuilder<QRCodeWidgetWorker>().build()
                        WorkManager.getInstance(context).enqueue(qRCodeWidgetWorker)
                        val tileWidgetWorker = OneTimeWorkRequestBuilder<TileWidgetWorker>().build()
                        WorkManager.getInstance(context).enqueue(tileWidgetWorker)
                        bitmap
                    },
                    scannedCode = scannedCode,
                    onScanClick = {
                        viewModel.scanBarcode(context) { showBarcodeScanErrorMessage = it }
                        trackAction(Analytics.Action.ScanCodeEdit, isProPurchased)
                    },
                    onChangeMade = {
                        haveSheetChangesBeenMade = true
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
                onDismissRequest = {
                    showConfirmChanges = false
                },
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

        if (showWelcomeDialog == true) {
            WelcomeDialog(
                isProPurchased = isProPurchased,
                onProPurchaseClick = onProPurchaseClick,
                onDismissRequest = {
                    showWelcomeDialog = false
                },
                saveShowWelcomeOnStart = { value ->
                    viewModel.updateShowWelcomeOnStart(!value)
                },
                windowSizeClass = windowSizeClass,
            )
        }
    }
}
