package dev.veryniche.quickqr.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import com.airbnb.android.showkase.models.Showkase
import dev.veryniche.quickqr.util.AboutAppText
import dev.veryniche.quickqr.util.UnorderedListText
import dev.veryniche.quickqr.BuildConfig
import dev.veryniche.quickqr.core.theme.Dimen
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.showkase.getBrowserIntent
import dev.veryniche.quickqr.util.Analytics
import dev.veryniche.quickqr.util.TrackedScreen
import dev.veryniche.quickqr.util.trackScreenView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollableState = rememberScrollState()

    TrackedScreen {
        trackScreenView(name = Analytics.Screen.About)
    }
    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                navigationIcon = { onNavigateBack.invoke() }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(Dimen.spacing),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(scrollableState)
                .padding(Dimen.spacingDouble)
        ) {
            Text(
                text = stringResource(id = R.string.about_subtitle),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            AboutAppText()
            Text(
                text = stringResource(id = R.string.about_developer_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(text = stringResource(id = R.string.about_developer))
            Text(
                text = stringResource(id = R.string.welcome_coming_soon_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            UnorderedListText(
                textLines = listOf(
                    R.string.welcome_coming_soon_ul_1,
                    R.string.welcome_coming_soon_ul_2
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimen.spacing)
            )
            Text(
                text = stringResource(id = R.string.about_remove_ads_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(text = stringResource(id = R.string.about_remove_ads))
            if (BuildConfig.DEBUG) {
                val context = LocalContext.current
                Spacer(modifier = Modifier.height(Dimen.spacingDouble))
                Button(content = {
                    Text(stringResource(id = R.string.navigate_showkase))
                }, onClick = {
                    startActivity(context, Showkase.getBrowserIntent(context), null)
                })
            }
        }
    }
}

@Preview(group = "About Screen", showBackground = true)
@Composable
fun AboutScreenPreview() {
    QuickQRTheme {
        AboutScreen({})
    }
}
