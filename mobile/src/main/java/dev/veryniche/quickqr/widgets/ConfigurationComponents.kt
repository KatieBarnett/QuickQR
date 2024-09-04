package dev.veryniche.quickqr.widgets

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.startActivity
import dev.veryniche.quickqr.MainActivity
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.components.BannerAd
import dev.veryniche.quickqr.components.BannerAdLocation
import dev.veryniche.quickqr.components.Tile
import dev.veryniche.quickqr.components.TopAppBarTitle
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItem
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.theme.Dimen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectQRCode(
    tiles: List<QRCodeItem>,
    setWidgetQRCode: (QRCodeItem, Boolean) -> Unit,
    isProPurchased: Boolean = false,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    TopAppBarTitle(R.string.app_name)
                },
            )
        },
        modifier = modifier
    ) { contentPadding ->
        var useColoredBackground by remember { mutableStateOf(false) }
        Column(Modifier.padding(contentPadding)) {
            Text(
                text = stringResource(R.string.widget_configuration),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(Dimen.spacing)
            )
            Row(
                Modifier.padding(Dimen.spacing),
                horizontalArrangement = Arrangement.spacedBy(Dimen.spacing),
            ) {
                Text(
                    stringResource(R.string.widget_configuration_background),
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = useColoredBackground,
                    onCheckedChange = {
                        useColoredBackground = !useColoredBackground
                    }
                )
            }
            if (tiles.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = Dimen.tileWidthDefault),
                    contentPadding = PaddingValues(Dimen.qRDetailDisplaySpacing),
                    horizontalArrangement = Arrangement.spacedBy(Dimen.qRDetailDisplaySpacing),
                    verticalArrangement = Arrangement.spacedBy(Dimen.qRDetailDisplaySpacing),
                    modifier = modifier.weight(1f)
                ) {
                    items(tiles) { tile ->
                        Tile(
                            qrCodeItem = tile,
                            onClickOverride = {
                                setWidgetQRCode.invoke(tile, useColoredBackground)
                            },
                            longPressDetail = null,
                            longPressCode = null,
                        )
                    }
                    item {
                        Spacer(
                            Modifier.windowInsetsBottomHeight(
                                WindowInsets.systemBars
                            )
                        )
                    }
                }
            } else {
                Text(
                    text = stringResource(R.string.widget_configuration_empty),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(Dimen.spacing)
                )
                Button(onClick = {
                    startActivity(context, Intent(context, MainActivity::class.java), null)
                }) {
                    Text(text = stringResource(R.string.widget_configuration_empty_button))
                }
            }
            if (!isProPurchased) {
                BannerAd(location = BannerAdLocation.MainScreen)
            }
        }
    }
}

@Preview(group = "Full App", showSystemUi = true, showBackground = true)
@Composable
fun SelectQRCodePreview() {
    SelectQRCode(
        listOf(
            sampleQRCodeItem,
            sampleQRCodeItem,
            sampleQRCodeItem,
            sampleQRCodeItem,
            sampleQRCodeItem,
            sampleQRCodeItem,
            sampleQRCodeItem,
            sampleQRCodeItem,
        ),
        { _, _ -> }
    )
}

@Preview(group = "Full App", showSystemUi = true, showBackground = true)
@Composable
fun SelectQRCodeEmptyPreview() {
    SelectQRCode(
        listOf(),
        { _, _ -> }
    )
}
