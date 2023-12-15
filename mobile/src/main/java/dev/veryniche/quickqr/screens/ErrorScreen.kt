package dev.veryniche.quickqr.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.veryniche.quickqr.R

@Composable
fun GenericErrorScreen(modifier: Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        Text(text = stringResource(id = R.string.generic_error))
    }
}
