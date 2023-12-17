package dev.veryniche.quickqr.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItem
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.model.QRColor
import dev.veryniche.quickqr.core.model.QRIcon
import dev.veryniche.quickqr.core.model.getRandomColor
import dev.veryniche.quickqr.core.model.getRandomIcon
import dev.veryniche.quickqr.core.theme.Dimen
import dev.veryniche.quickqr.core.theme.Dimen.AddCodeQRPadding
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.previews.PreviewComponent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Edit(
    initialItem: QRCodeItem? = null,
    onSaveClick: (
        name: String?,
        content: String?,
        icon: QRIcon,
        primaryColor: QRColor
    ) -> ImageBitmap?,
    onScanClick: () -> Unit,
    onIconClick: (QRIcon) -> Unit,
    onColorClick: (QRColor) -> Unit,
    onCloseClick: () -> Unit,
) {
    var imageBitmap by remember { mutableStateOf(initialItem?.imageBitmap) }
    var name by remember { mutableStateOf(initialItem?.name) }
    var content by remember { mutableStateOf(initialItem?.content) }
    var icon by remember { mutableStateOf(initialItem?.icon ?: getRandomIcon()) }
    var primaryColor by remember { mutableStateOf(initialItem?.primaryColor ?: getRandomColor()) }

    Column(
        verticalArrangement = Arrangement.spacedBy(Dimen.AddCodeQRVerticalSpacing),
        modifier = Modifier.padding(AddCodeQRPadding)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Dimen.AddCodeQRHorizontalSpacing),
            modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max)
        ) {
            Column {
                TextField(
                    value = name.orEmpty(),
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.label_name)) }
                )
                TextField(
                    value = content.orEmpty(),
                    onValueChange = { content = it },
                    label = { Text(stringResource(R.string.label_content)) }
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
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
                                    onIconClick.invoke(icon)
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
                                    onColorClick.invoke(primaryColor)
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
        Button(onClick = {
            onSaveClick.invoke(name, content, icon, primaryColor)?.let {
                imageBitmap = it
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.save_qr))
        }
    }
}

@PreviewComponent
@Composable
fun AddNewPreview() {
    QuickQRTheme {
        Surface {
            Edit(
                initialItem = sampleQRCodeItem,
                onSaveClick = { _, _, _, _ -> null },
                onScanClick = {},
                onIconClick = {},
                onColorClick = {},
                onCloseClick = {},
            )
        }
    }
}
