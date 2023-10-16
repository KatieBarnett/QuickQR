package dev.veryniche.quickqr.components

import android.graphics.Bitmap
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Email
import androidx.compose.material.icons.twotone.Info
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.core.Constants
import dev.veryniche.quickqr.core.decodeImage
import dev.veryniche.quickqr.core.theme.Dimen
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import kotlin.io.encoding.ExperimentalEncodingApi


@Composable
fun SideQRCode(
    image: Bitmap,
    barcodeContent: String,
    background: Color,
    modifier: Modifier = Modifier,
) {
    Surface (
        color = background,
        modifier = modifier.padding(Dimen.QRCodeDisplayPadding)
    ) {
        Image(image.asImageBitmap(), barcodeContent)
    }
}

@Composable
fun SideDetails(
    name: String,
    content: String?,
    icon: ImageVector,
    background: Color,
    modifier: Modifier = Modifier,
) {
    Surface (
        color = background,
        modifier = modifier.padding(Dimen.QRCodeDisplayPadding)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.padding(Dimen.QRDetailDisplayPadding)
        ) {
            Image(icon, contentDescription = content, modifier = Modifier.fillMaxSize(0.3f))
            Text(name)
            content?.let {
                Text(content)
            }
        }
    }
}

@Composable
fun SideAdd(modifier: Modifier) {
    SideDetails(
        name = stringResource(R.string.add_qr),
        content = null,
        icon = Icons.Outlined.Add,
        background = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}


@Composable
fun Tile(
    sideFront: @Composable (Modifier) -> Unit,
    sideBack: @Composable (Modifier) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showingFront by remember { mutableStateOf<Boolean?>(null) }
    var flipRotation by remember { mutableFloatStateOf(0f) }
    val animationSpecFlip = tween<Float>(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))

    showingFront?.let {
        LaunchedEffect(showingFront) {
            if (showingFront != false) {
                // Do the flip
                animate(
                    initialValue = 0f,
                    targetValue = 180f,
                    animationSpec = animationSpecFlip
                ) { value: Float, _: Float ->
                    flipRotation = value
                }
            } else {
                animate(
                    initialValue = 180f,
                    targetValue = 0f,
                    animationSpec = animationSpecFlip
                ) { value: Float, _: Float ->
                    flipRotation = value
                }
            }
        }
    }

    Box(modifier.clickable { showingFront = !(showingFront ?: false) }) {
        val animatedModifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationY = flipRotation
                cameraDistance = 8 * density
            }
        if (flipRotation < 90f) {
            sideFront.invoke(animatedModifier)
        } else {
            // Rotate the action card back again so it does not appear reversed
            sideBack.invoke(animatedModifier.graphicsLayer {
                rotationY = 180f
            })
        }
    }
}

@Composable
fun AddTile(
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SideAdd(modifier = modifier.clickable {
        onTap.invoke()
    })
}

@Preview
@Composable
fun AddTilePreview() {
    QuickQRTheme {
        Box(Modifier.aspectRatio(1f)) {
            AddTile({}, Modifier.fillMaxSize())
        }
    }
}

@Preview
@Composable
fun TilePreview() {
    QuickQRTheme {
        Box(Modifier.aspectRatio(1f)) {
            Tile(
                sideFront = { modifier ->
                    SideDetails(
                        name = Constants.sampleName,
                        content = Constants.sampleUrl,
                        icon = Icons.Outlined.Info,
                        background = MaterialTheme.colorScheme.primary,
                        modifier = modifier
                    )
                },
                sideBack = { modifier ->
                    SideQRCode(
                        image = Constants.sampleQRCodeBase.decodeImage(),
                        barcodeContent = Constants.sampleUrl,
                        background = MaterialTheme.colorScheme.primary,
                        modifier = modifier
                    )
                },
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}