package dev.veryniche.quickqr.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItem
import dev.veryniche.quickqr.core.model.Icon
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.model.getRandomIcon
import dev.veryniche.quickqr.core.theme.Dimen
import dev.veryniche.quickqr.core.theme.Dimen.AddCodeQRPadding
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.core.theme.md_theme_light_onPrimary
import dev.veryniche.quickqr.core.theme.md_theme_light_primary

@Composable
fun Edit(
    initialItem: QRCodeItem? = null,
    onSaveClick: (
        name: String?,
        content: String?,
        icon: Icon,
        primaryColor: Color,
        secondaryColor: Color
    ) -> ImageBitmap?,
    onScanClick: () -> Unit,
    onCloseClick: () -> Unit,
) {
    var imageBitmap by remember { mutableStateOf(initialItem?.imageBitmap) }
    var name by remember { mutableStateOf(initialItem?.name) }
    var content by remember { mutableStateOf(initialItem?.content) }
    var icon by remember { mutableStateOf(initialItem?.icon ?: getRandomIcon()) }
    var primaryColor by remember { mutableStateOf(initialItem?.primaryColor ?: md_theme_light_primary) }
    var secondaryColor by remember { mutableStateOf(initialItem?.secondaryColor ?: md_theme_light_onPrimary) }

    Column(
        verticalArrangement = Arrangement.spacedBy(Dimen.AddCodeQRVerticalSpacing),
        modifier = Modifier.padding(AddCodeQRPadding)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(Dimen.AddCodeQRHorizontalSpacing)) {
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
            }
            Box(Modifier.size(Dimen.AddCodeQRSize)) {
                if (imageBitmap != null) {
                    imageBitmap?.let {
                        Image(it, content, Modifier.fillMaxSize())
                    }
                } else {
                    Image(
                        Icon.SCAN.vector,
                        contentDescription = content,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                onScanClick.invoke()
                            }
                    )
                }
            }
        }
        Button(onClick = {
            onSaveClick.invoke(name, content, icon, primaryColor, secondaryColor)?.let {
                imageBitmap = it
            }
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(stringResource(R.string.save_qr))
        }
    }
//
//    Button(onClick = onCloseClick) {
//        Text("Close")
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AddNewPreview() {
    QuickQRTheme {
        Surface {
            Edit(
                initialItem = sampleQRCodeItem,
                onSaveClick = { _, _, _, _, _ -> null },
                onScanClick = {},
                onCloseClick = {},
            )
        }
    }
}
