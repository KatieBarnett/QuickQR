package dev.veryniche.quickqr.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.ScannedCode
import dev.veryniche.quickqr.components.ColorSelectorDialog
import dev.veryniche.quickqr.components.IconSelectorDialog
import dev.veryniche.quickqr.components.QRColorShape
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItem
import dev.veryniche.quickqr.core.decodeImage
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.model.QRColor
import dev.veryniche.quickqr.core.model.QRIcon
import dev.veryniche.quickqr.core.model.getRandomColor
import dev.veryniche.quickqr.core.model.getRandomIcon
import dev.veryniche.quickqr.core.theme.Dimen
import dev.veryniche.quickqr.core.theme.Dimen.AddCodeQRPadding
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.previews.PreviewComponent
import dev.veryniche.quickqr.util.trackColorChoice
import dev.veryniche.quickqr.util.trackIconChoice
import java.util.Date

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditSheet(
    initialItem: QRCodeItem? = null,
    scannedCode: ScannedCode?,
    onSaveClick: (
        name: String?,
        content: String?,
        icon: QRIcon,
        primaryColor: QRColor
    ) -> ImageBitmap?,
    onDeleteClick: () -> Unit,
    onScanClick: () -> Unit,
) {
    var imageBitmap by remember(scannedCode) {
        mutableStateOf(scannedCode?.base64?.decodeImage()?.asImageBitmap() ?: initialItem?.imageBitmap)
    }
    var name by remember { mutableStateOf(initialItem?.name) }
    var content by remember(scannedCode) { mutableStateOf(scannedCode?.content ?: initialItem?.content) }
    var icon by remember { mutableStateOf(initialItem?.icon ?: getRandomIcon()) }
    var primaryColor by remember { mutableStateOf(initialItem?.primaryColor ?: getRandomColor()) }

    var showColorSelector by remember { mutableStateOf(false) }
    var showIconSelector by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.spacedBy(Dimen.AddCodeQRVerticalSpacing),
        modifier = Modifier.padding(AddCodeQRPadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimen.AddCodeQRHorizontalSpacing),
            modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                OutlinedTextField(
                    value = name.orEmpty(),
                    onValueChange = { name = it },
                    isError = name.isNullOrBlank(),
                    label = { Text(stringResource(R.string.label_name)) },
                    supportingText = {
                        if (name.isNullOrBlank()) {
                            Text(
                                text = stringResource(R.string.validation_message_name),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                OutlinedTextField(
                    value = content.orEmpty(),
                    onValueChange = { content = it },
                    isError = content.isNullOrBlank(),
                    label = { Text(stringResource(R.string.label_content)) },
                    supportingText = {
                        if (content.isNullOrBlank()) {
                            Text(
                                text = stringResource(R.string.validation_message_content),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.edit_icon))
                        Image(
                            imageVector = icon.vector,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface),
                            contentDescription = icon.name,
                            modifier = Modifier
                                .size(48.dp)
                                .aspectRatio(1f)
                                .clickable {
                                    showIconSelector = true
                                }
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.edit_colour))
                        QRColorShape(
                            primaryColor,
                            false,
                            Modifier
                                .padding(top = 4.dp, bottom = 4.dp)
                                .size(40.dp)
                                .clickable {
                                    showColorSelector = true
                                }
                        )
                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxHeight()
                    .clickable {
                        onScanClick.invoke()
                    }
            ) {
                if (imageBitmap != null) {
                    imageBitmap?.let {
                        Image(
                            it,
                            content,
                            Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        )
                    }
                    Text(
                        text = stringResource(R.string.click_to_rescan),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Image(
                        QRIcon.SCAN.vector,
                        contentDescription = content,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clickable {
                                onScanClick.invoke()
                            }
                    )
                    Text(
                        text = stringResource(R.string.click_to_scan),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        Button(
            enabled = !name.isNullOrBlank() && !content.isNullOrBlank(),
            onClick = {
                onSaveClick.invoke(name, content, icon, primaryColor).let {
                    imageBitmap = it
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_qr))
        }
        TextButton(
            onClick = {
                showDeleteConfirmation = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete_qr))
        }
//        Spacer(
//            Modifier.windowInsetsBottomHeight(
//                WindowInsets.systemBars
//            )
//        )
    }

    if (showColorSelector) {
        ColorSelectorDialog(
            onDismissRequest = { showColorSelector = false }
        ) {
            primaryColor = it
            trackColorChoice(it)
            showColorSelector = false
        }
    }

    if (showIconSelector) {
        IconSelectorDialog(
            onDismissRequest = { showIconSelector = false }
        ) {
            icon = it
            trackIconChoice(it)
            showIconSelector = false
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text(stringResource(R.string.confirm_delete_title)) },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteClick.invoke()
                }) {
                    Text(stringResource(R.string.confirm_delete_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteConfirmation = false
                }) {
                    Text(stringResource(R.string.confirm_delete_dismiss))
                }
            }
        )
    }
}

@PreviewComponent
@Composable
fun EditSheetPreview() {
    QuickQRTheme {
        Surface {
            EditSheet(
                initialItem = sampleQRCodeItem,
                scannedCode = null,
                onSaveClick = { _, _, _, _ -> null },
                onScanClick = { Pair(null, null) },
                onDeleteClick = {},
            )
        }
    }
}

@PreviewComponent
@Composable
fun EditSheetErrorPreview() {
    QuickQRTheme {
        Surface {
            EditSheet(
                initialItem = QRCodeItem(
                    id = 0,
                    name = "",
                    content = "",
                    imageBase64 = "",
                    icon = QRIcon.Star,
                    primaryColor = QRColor.Violet,
                    lastModified = Date()
                ),
                scannedCode = null,
                onSaveClick = { _, _, _, _ -> null },
                onDeleteClick = {},
                onScanClick = { Pair(null, null) },
            )
        }
    }
}
