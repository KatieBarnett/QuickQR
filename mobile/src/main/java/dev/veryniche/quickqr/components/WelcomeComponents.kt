package dev.veryniche.quickqr.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.core.edit
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.analytics.UnorderedListText
import dev.veryniche.quickqr.core.theme.Dimen
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.dataStore
import dev.veryniche.quickqr.screens.AboutHeading
import dev.veryniche.quickqr.util.Settings
import kotlinx.coroutines.launch

@Composable
fun WelcomeDialog(onDismissRequest: () -> Unit) {
    AnimatedTransitionDialog(onDismissRequest = onDismissRequest) { dialogHelper ->
        WelcomeDialogContent(onDismissRequest)
    }
}

@Composable
fun WelcomeDialogContent(onDismissRequest: () -> Unit) {
    val scrollableState = rememberScrollState()
    var checkedState by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Card(
        modifier = Modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(Dimen.spacingDouble)
                .verticalScroll(scrollableState)
        ) {
            AboutHeading(R.string.welcome_title)
            Text(
                text = stringResource(id = R.string.about_instructions),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            UnorderedListText(
                textLines = listOf(
                    R.string.about_instructions_ul_1,
                    R.string.about_instructions_ul_2,
                    R.string.about_instructions_ul_3,
                    R.string.about_instructions_ul_4,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimen.spacing)
            )
            AboutHeading(R.string.about_coming_soon_title)
            UnorderedListText(
                textLines = listOf(
                    R.string.about_coming_soon_ul_1,
                    R.string.about_coming_soon_ul_2
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimen.spacing)
            )

            AboutHeading(R.string.about_pro_version_title)
            Button(content = {
                Text(text = stringResource(id = R.string.about_get_pro_version))
            }, onClick = {
            })
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(id = R.string.welcome_dont_show_again)
                )
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = {
                        checkedState = it
                    }
                )
            }
            Button(content = {
                Text(stringResource(id = R.string.welcome_close))
            }, onClick = {
                coroutineScope.launch {
                    context.dataStore.edit { settings ->
                        settings[Settings.KEY_SHOW_WELCOME] = !checkedState
                    }
                }
                onDismissRequest.invoke()
            })
        }
    }
}

@Preview(group = "Welcome Dialog", showBackground = true)
@Composable
fun WelcomeDialogPreview() {
    QuickQRTheme {
        WelcomeDialogContent({})
    }
}
