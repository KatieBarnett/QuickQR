package dev.veryniche.quickqr.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.core.model.QRColor
import dev.veryniche.quickqr.core.model.QRIcon
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.previews.PreviewComponent

@Composable
fun AddContainer(
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        content.invoke(this)
        Spacer(
            Modifier.windowInsetsBottomHeight(
                WindowInsets.systemBars
            )
        )
    }
}

@Composable
fun AddChoice(
    onScanClick: () -> Unit,
    onEnterUrlClick: () -> Unit,
    modifier: Modifier
) {
    AddContainer({
        Button(onClick = { onScanClick.invoke() }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(id = R.string.add_scan))
        }
        Text(
            text = stringResource(id = R.string.add_or),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { onEnterUrlClick.invoke() }, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(id = R.string.add_enter_url))
        }
    }, modifier)
}

@Composable
fun AddEnterUrl(
    onNextClick: (String) -> Unit,
    modifier: Modifier
) {
    var content by remember { mutableStateOf("") }

    AddContainer({
        Text(stringResource(id = R.string.add_enter_url))
        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text(stringResource(R.string.label_content)) },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { onNextClick.invoke(content) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.add_next))
        }
    }, modifier)
}

@Composable
fun AddSelectColour(
    onColorClick: (QRColor) -> Unit,
    modifier: Modifier
) {
    AddContainer({
        Text(stringResource(id = R.string.add_select_color))
        ColorSelector(columns = 7, onColorClick = {
            onColorClick.invoke(it)
        }, modifier = Modifier.fillMaxWidth())
    }, modifier)
}

@Composable
fun AddSelectIcon(
    onIconClick: (QRIcon) -> Unit,
    modifier: Modifier
) {
    AddContainer({
        Text(stringResource(id = R.string.add_select_icon))
        IconSelector(columns = 7, onIconClick = {
            onIconClick.invoke(it)
        }, modifier = Modifier.fillMaxWidth())
    }, modifier)
}

@Composable
fun AddEnterName(
    onSaveClick: (String) -> Unit,
    modifier: Modifier
) {
    var name by remember { mutableStateOf("") }
    AddContainer({
        Text(stringResource(id = R.string.add_enter_name))
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.label_name)) },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { onSaveClick.invoke(name) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.add_save))
        }
    }, modifier)
}

@PreviewComponent
@Composable
fun AddChoicePreview() {
    QuickQRTheme {
        Surface {
            AddChoice({}, {}, modifier = Modifier)
        }
    }
}

@PreviewComponent
@Composable
fun AddEnterUrlPreview() {
    QuickQRTheme {
        Surface {
            AddEnterUrl({}, modifier = Modifier)
        }
    }
}

@PreviewComponent
@Composable
fun AddSelectColourPreview() {
    QuickQRTheme {
        Surface {
            AddSelectColour({}, modifier = Modifier)
        }
    }
}

@PreviewComponent
@Composable
fun AddSelectIconPreview() {
    QuickQRTheme {
        Surface {
            AddSelectIcon({}, modifier = Modifier)
        }
    }
}

@PreviewComponent
@Composable
fun AddEnterNamePreview() {
    QuickQRTheme {
        Surface {
            AddEnterName({}, modifier = Modifier)
        }
    }
}
