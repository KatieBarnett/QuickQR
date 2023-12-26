package dev.veryniche.quickqr.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.core.model.QRColor
import dev.veryniche.quickqr.core.model.QRIcon
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.previews.PreviewComponent

@Composable
fun DialogContainer(
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(onDismissRequest = {
        onDismissRequest.invoke()
    }) {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                content.invoke(this)
            }
        }
    }
}

@Composable
fun ColorSelectorDialog(
    onDismissRequest: () -> Unit,
    onSelected: (QRColor) -> Unit
) {
    DialogContainer({ onDismissRequest.invoke() }) {
        Text(stringResource(id = R.string.add_select_color))
        ColorSelector(columns = 5, onColorClick = {
            onSelected.invoke(it)
        }, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun IconSelectorDialog(
    onDismissRequest: () -> Unit,
    onSelected: (QRIcon) -> Unit
) {
    DialogContainer({ onDismissRequest.invoke() }) {
        Text(stringResource(id = R.string.add_select_icon))
        IconSelector(columns = 5, onIconClick = {
            onSelected.invoke(it)
        }, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun MessageDialog(
    text: String,
    buttonText: String,
    onDismissRequest: () -> Unit,
    onButtonClick: () -> Unit
) {
    DialogContainer({ onDismissRequest.invoke() }) {
        Text(text,
            style = MaterialTheme.typography.bodyLarge)
        Button(onClick = { onButtonClick.invoke() }, modifier = Modifier.fillMaxWidth()) {
            Text(buttonText)
        }
    }
}

@PreviewComponent
@Composable
fun ColorSelectorDialogPreview() {
    QuickQRTheme {
        ColorSelectorDialog(
            onDismissRequest = { }
        ) {
        }
    }
}

@PreviewComponent
@Composable
fun IconSelectorDialogPreview() {
    QuickQRTheme {
        IconSelectorDialog(
            onDismissRequest = { }
        ) {
        }
    }
}

@PreviewComponent
@Composable
fun MessageDialogPreview() {
    QuickQRTheme {
        MessageDialog(
            text = "This is some sample message text",
            buttonText = "Close",
            onDismissRequest = { },
            onButtonClick = { }
        )
    }
}
