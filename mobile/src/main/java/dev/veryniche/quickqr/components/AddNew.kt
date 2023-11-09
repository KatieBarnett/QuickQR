package dev.veryniche.quickqr.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.veryniche.quickqr.core.theme.QuickQRTheme

@Composable
fun ColumnScope.AddNew(
    onSaveClick: () -> Unit,
    onScanClick: () -> Unit,
    onCloseClick: () -> Unit,
) {

    Button(onClick = onScanClick) {
        Text("Scan")
    }

    Button(onClick = onSaveClick) {
        Text("Save")
    }

     Button(onClick = onCloseClick) {
         Text("Close")
     }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AddNewPreview() {
    QuickQRTheme {
        ModalBottomSheet(onDismissRequest = { }) {
            AddNew(
                onSaveClick = {},
                onScanClick = {},
                onCloseClick = {},
            )
        }

    }
}