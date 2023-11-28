package dev.veryniche.quickqr.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.startActivity
import com.airbnb.android.showkase.models.Showkase
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.showkase.getBrowserIntent

@Composable
fun ShowkaseActionIcon() {
    val context = LocalContext.current
    IconButton(
        onClick = {
            startActivity(context, Showkase.getBrowserIntent(context), null)
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Info,
            contentDescription = stringResource(id = R.string.navigate_about)
        )
    }
}
