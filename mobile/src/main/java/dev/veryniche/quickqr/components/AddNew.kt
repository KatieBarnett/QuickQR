package dev.veryniche.quickqr.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.veryniche.quickqr.core.theme.QuickQRTheme

@Composable
fun ColumnScope.AddNew(
    onSaveClick: () -> Unit,
    onScanClick: () -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

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
                modifier = Modifier.fillMaxSize(),
            )
        }

    }
}