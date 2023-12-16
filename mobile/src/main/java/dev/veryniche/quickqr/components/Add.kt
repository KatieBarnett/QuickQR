package dev.veryniche.quickqr.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.core.model.Icon
import dev.veryniche.quickqr.core.model.QRColor
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.previews.PreviewComponent

@Composable
fun AddChoice(
    onScanClick: () -> Unit,
    onEnterUrlClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(16.dp)
    ) {
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
    }
}

@Composable
fun AddEnterUrl(
    onNextClick: (String) -> Unit,
    modifier: Modifier
) {
    var content by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(16.dp)
    ) {
        Text(stringResource(id = R.string.add_select_color))
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
    }
}

@Composable
fun AddSelectColour(
    onColorClick: (QRColor) -> Unit,
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(16.dp)
    ) {
        Text(stringResource(id = R.string.add_select_color))
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(QRColor.entries.filter { it.showInList }.toTypedArray()) { colorItem ->
                QRColorShape(
                    colorItem,
                    false,
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            onColorClick.invoke(colorItem)
                        }
                )
            }
        }
    }
}

@Composable
fun AddSelectIcon(
    onIconClick: (Icon) -> Unit,
    modifier: Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(16.dp)
    ) {
        Text(stringResource(id = R.string.add_select_icon))
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(Icon.entries.filter { it.showInList }.toTypedArray()) { icon ->
                Image(
                    icon.vector,
                    contentDescription = icon.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable {
                            onIconClick.invoke(icon)
                        }
                )
            }
        }
    }
}

@Composable
fun AddEnterName(
    onSaveClick: (String) -> Unit,
    modifier: Modifier
) {
    var name by remember { mutableStateOf("") }
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.padding(16.dp)
    ) {
        Text(stringResource(id = R.string.add_select_color))
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
    }
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
