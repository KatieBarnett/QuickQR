package dev.veryniche.quickqr.components

import android.graphics.Bitmap
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItem
import dev.veryniche.quickqr.core.decodeImage
import dev.veryniche.quickqr.core.model.Icon
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.theme.Dimen
import dev.veryniche.quickqr.core.theme.QuickQRTheme

@Composable
fun SideQRCode(
    image: Bitmap,
    barcodeContent: String,
    background: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = background,
            contentColor = contentColor
        ),
        modifier = modifier
    ) {
        Image(
            bitmap = image.asImageBitmap(),
            contentDescription = barcodeContent,
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimen.QRCodeDisplayPadding)
        )
    }
}

@Composable
fun SideDetails(
    name: String,
    content: String?,
    icon: ImageVector,
    background: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = background,
            contentColor = contentColor
        ),
        modifier = modifier.padding(Dimen.QRCodeDisplayPadding)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(Dimen.QRDetailDisplayPadding)
                .fillMaxSize()
        ) {
            Image(
                imageVector = icon,
                colorFilter = ColorFilter.tint(contentColor),
                contentDescription = content,
                modifier = Modifier.fillMaxSize(0.3f)
            )
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                text = name,
                color = contentColor,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.weight(0.3f)
            )
            content?.let {
                Text(
                    text = it,
                    color = contentColor,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier.weight(0.3f)
                )
            }
        }
    }
}

@Composable
fun SideAdd(modifier: Modifier) {
    SideDetails(
        name = stringResource(R.string.add_qr),
        content = null,
        icon = Icon.ADD_QR_CODE.vector,
        background = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

@Composable
fun Tile(
    qrCodeItem: QRCodeItem,
    modifier: Modifier = Modifier,
) {
    Tile(
        sideFront = { modifier ->
            SideDetails(
                name = qrCodeItem.name,
                content = qrCodeItem.content,
                icon = qrCodeItem.icon.vector,
                background = qrCodeItem.primaryColor,
                contentColor = qrCodeItem.secondaryColor,
                modifier = modifier
            )
        },
        sideBack = { modifier ->
            SideQRCode(
                image = qrCodeItem.imageBase64.decodeImage(),
                barcodeContent = qrCodeItem.content,
                background = qrCodeItem.primaryColor,
                contentColor = qrCodeItem.secondaryColor,
                modifier = modifier
            )
        },
        modifier = modifier,
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

    Box(
        modifier
            .aspectRatio(1f)
            .clickable { showingFront = !(showingFront ?: false) }
    ) {
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
            sideBack.invoke(
                animatedModifier.graphicsLayer {
                    rotationY = 180f
                }
            )
        }
    }
}

@Composable
fun AddTile(
    onTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SideAdd(
        modifier = modifier
            .aspectRatio(1f)
            .clickable {
                onTap.invoke()
            }
    )
}

// @Preview
// @Composable
// fun AddTilePreview() {
//    QuickQRTheme {
//        AddTile({}, Modifier)
//    }
// }

//@Preview
//@Composable
//fun TileFrontPreview() {
//    QuickQRTheme {
//        SideDetails(
//            name = sampleQRCodeItem.name,
//            content = sampleQRCodeItem.content,
//            icon = sampleQRCodeItem.icon.vector,
//            background = sampleQRCodeItem.primaryColor,
//            contentColor = sampleQRCodeItem.secondaryColor,
//            modifier = Modifier.aspectRatio(1f)
//        )
//    }
//}

// @Preview
// @Composable
// fun TileBackPreview() {
//    QuickQRTheme {
//        SideQRCode(
//            image = sampleQRCodeItem.imageBase64.decodeImage(),
//            barcodeContent = sampleQRCodeItem.content,
//            background = sampleQRCodeItem.primaryColor,
//            contentColor = sampleQRCodeItem.secondaryColor,
//            modifier = Modifier.aspectRatio(1f)
//        )
//    }
// }
//
// @Preview
// @Composable
// fun TilePreview() {
//    QuickQRTheme {
//        Tile(
//            qrCodeItem = sampleQRCodeItem,
//            modifier = Modifier,
//        )
//    }
// }
