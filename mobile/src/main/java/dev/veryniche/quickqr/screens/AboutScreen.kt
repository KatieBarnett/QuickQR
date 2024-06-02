package dev.veryniche.quickqr.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.core.content.ContextCompat.startActivity
import com.airbnb.android.showkase.models.Showkase
import dev.veryniche.quickqr.BuildConfig
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.analytics.Analytics
import dev.veryniche.quickqr.analytics.TrackedScreen
import dev.veryniche.quickqr.analytics.UnorderedListText
import dev.veryniche.quickqr.analytics.trackAction
import dev.veryniche.quickqr.analytics.trackScreenView
import dev.veryniche.quickqr.components.NavigationIcon
import dev.veryniche.quickqr.components.TopAppBarTitle
import dev.veryniche.quickqr.core.theme.Dimen
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.previews.PreviewScreen
import dev.veryniche.quickqr.showkase.getBrowserIntent

@Composable
fun AboutHeading(textRes: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = textRes),
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun AboutText(textRes: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = textRes),
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier.fillMaxWidth()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    isProPurchased: Boolean,
    onProPurchaseClick: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    modifier: Modifier = Modifier
) {
    val scrollableState = rememberScrollState()
    val context = LocalContext.current
    TrackedScreen {
        trackScreenView(name = Analytics.Screen.About)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TopAppBarTitle(R.string.about_title)
                },
                navigationIcon = { NavigationIcon { onNavigateBack.invoke() } }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
                text = stringResource(id = R.string.about_instructions),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
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

//            AboutHeading(R.string.about_coming_soon_title)
//            UnorderedListText(
//                textLines = listOf(
//                    R.string.about_coming_soon_ul_1,
//                    R.string.about_coming_soon_ul_2
//                ),
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = Dimen.spacing)
//            )

            if (!isProPurchased) {
                AboutHeading(R.string.about_pro_version_title)
                Button(content = {
                    Text(text = stringResource(id = R.string.about_get_pro_version))
                }, onClick = {
                    trackAction(Analytics.Action.AboutProVersion)
                    onProPurchaseClick.invoke()
                })
            }

            AboutHeading(R.string.about_developer_title)
            AboutText(R.string.about_developer_text)
            val aboutDeveloperUrl = stringResource(id = R.string.about_developer_url)
            Button(content = {
                Text(text = stringResource(id = R.string.about_developer))
            }, onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(aboutDeveloperUrl))
                context.startActivity(intent)
            })

            val privacyPolicyUrl = stringResource(id = R.string.about_privacy_policy_url)
            AboutHeading(R.string.about_privacy_policy_title)
            Button(content = {
                Text(text = stringResource(id = R.string.about_privacy_policy))
            }, onClick = {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyPolicyUrl))
                context.startActivity(intent)
            })

            if (BuildConfig.DEBUG) {
                Spacer(modifier = Modifier.height(Dimen.spacingDouble))
                Button(content = {
                    Text(stringResource(id = R.string.navigate_showkase))
                }, onClick = {
                    startActivity(context, Showkase.getBrowserIntent(context), null)
                })
            }

            Spacer(modifier = Modifier.height(Dimen.spacingDouble))
            Text(
                text = stringResource(id = R.string.about_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = modifier.fillMaxWidth()
            )
        }
    }
}

@PreviewScreen
@Composable
fun AboutScreenFreePreview() {
    QuickQRTheme {
        AboutScreen(isProPurchased = false, onNavigateBack = {}, onProPurchaseClick = {})
    }
}

@PreviewScreen
@Composable
fun AboutScreenProPreview() {
    QuickQRTheme {
        AboutScreen(isProPurchased = true, onNavigateBack = {}, onProPurchaseClick = {})
    }
}
