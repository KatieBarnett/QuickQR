package dev.veryniche.quickqr.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.veryniche.quickqr.R
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItem
import dev.veryniche.quickqr.core.model.QRIcon
import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.theme.Dimen
import dev.veryniche.quickqr.core.theme.QuickQRTheme
import dev.veryniche.quickqr.previews.PreviewTile

@Composable
fun SideQRCode(
    image: ImageBitmap,
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
            bitmap = image,
            contentDescription = barcodeContent,
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimen.qRCodeDisplayPadding)
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
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(Dimen.qRDetailDisplayPadding)
                .fillMaxSize()
        ) {
            Image(
                imageVector = icon,
                colorFilter = ColorFilter.tint(contentColor),
                contentDescription = content,
                modifier = Modifier.fillMaxSize(0.3f)
            )
            Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(0.3f)) {
                Text(
                    text = name,
                    color = contentColor,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                )
            }
            content?.let {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(0.3f)) {
                    Text(
                        text = it,
                        color = contentColor,
                        textAlign = TextAlign.Center,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun SideAdd(modifier: Modifier) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .padding(Dimen.qRDetailDisplayPadding)
                .fillMaxSize()
        ) {
            Image(
                imageVector = QRIcon.ADD_QR_CODE.vector,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                contentDescription = stringResource(R.string.add_qr),
                modifier = Modifier.fillMaxSize(0.3f)
            )
            Box(contentAlignment = Alignment.Center, modifier = Modifier.weight(0.3f)) {
                Text(
                    text = stringResource(R.string.add_qr),
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun Tile(
    qrCodeItem: QRCodeItem,
    longPressDetail: (QRCodeItem) -> Unit,
    longPressCode: (QRCodeItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Tile(
        sideFront = { modifier ->
            SideDetails(
                name = qrCodeItem.name,
                content = qrCodeItem.content,
                icon = qrCodeItem.icon.vector,
                background = qrCodeItem.primaryColor.color,
                contentColor = qrCodeItem.secondaryColor,
                modifier = modifier
            )
        },
        sideBack = { modifier ->
            SideQRCode(
                image = qrCodeItem.imageBitmap,
                barcodeContent = qrCodeItem.content,
                background = qrCodeItem.primaryColor.color,
                contentColor = qrCodeItem.secondaryColor,
                modifier = modifier
            )
        },
        longPressDetail = {
            longPressDetail.invoke(qrCodeItem)
        },
        longPressCode = {
            longPressCode.invoke(qrCodeItem)
        },
        modifier = modifier,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Tile(
    sideFront: @Composable (Modifier) -> Unit,
    sideBack: @Composable (Modifier) -> Unit,
    longPressDetail: () -> Unit,
    longPressCode: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showingFront by remember { mutableStateOf<Boolean?>(null) }
    var flipRotation by remember { mutableFloatStateOf(0f) }
    val animationSpecFlip = tween<Float>(1000, easing = CubicBezierEasing(0.4f, 0.0f, 0.8f, 0.8f))
    val haptics = LocalHapticFeedback.current
    LaunchedEffect(showingFront) {
        if (showingFront == false) {
            // Do the flip
            animate(
                initialValue = 0f,
                targetValue = 180f,
                animationSpec = animationSpecFlip
            ) { value: Float, _: Float ->
                flipRotation = value
            }
        } else if (showingFront == true) {
            animate(
                initialValue = 180f,
                targetValue = 0f,
                animationSpec = animationSpecFlip
            ) { value: Float, _: Float ->
                flipRotation = value
            }
        }
    }

    Box(
        modifier
            .aspectRatio(1f)
            .combinedClickable(
                onClick = {
                    showingFront = !(showingFront ?: true)
                },
                onLongClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (showingFront != false) {
                        longPressDetail.invoke()
                    } else {
                        longPressCode.invoke()
                    }
                }
            )
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

@PreviewTile
@Composable
fun AddTilePreview() {
    QuickQRTheme {
        AddTile({}, Modifier)
    }
}

class TileParameterProvider : PreviewParameterProvider<QRCodeItem> {
    override val values = sequenceOf(
        sampleQRCodeItem,
//        sampleQRCodeItem.copy(name = "Very very very very very long name"),
//        sampleQRCodeItem.copy(content = "Very very very very very long content"),
//        sampleQRCodeItem.copy(
//            name = "Very very very very very long name",
//            content = "Very very very very very long content"
//        ),
    )
}

@PreviewTile
@Composable
fun TileFrontPreview() {
    QuickQRTheme {
        SideDetails(
            name = sampleQRCodeItem.name,
            content = sampleQRCodeItem.content,
            icon = sampleQRCodeItem.icon.vector,
            background = sampleQRCodeItem.primaryColor.color,
            contentColor = sampleQRCodeItem.secondaryColor,
            modifier = Modifier.aspectRatio(1f)
        )
    }
}

@PreviewTile
@Composable
fun TileFrontLongDetailsPreview() {
    QuickQRTheme {
        SideDetails(
            name = "Very very very very very long name",
            content = "Very very very very very long content",
            icon = sampleQRCodeItem.icon.vector,
            background = sampleQRCodeItem.primaryColor.color,
            contentColor = sampleQRCodeItem.secondaryColor,
            modifier = Modifier.aspectRatio(1f)
        )
    }
}

@PreviewTile
@Composable
fun TileBackPreview() {
    QuickQRTheme {
        SideQRCode(
            image = sampleQRCodeItem.imageBitmap,
            barcodeContent = sampleQRCodeItem.content,
            background = sampleQRCodeItem.primaryColor.color,
            contentColor = sampleQRCodeItem.secondaryColor,
            modifier = Modifier.aspectRatio(1f)
        )
    }
}

@PreviewTile
@Composable
fun TilePreview() {
    QuickQRTheme {
        Tile(
            qrCodeItem = sampleQRCodeItem,
            longPressCode = {},
            longPressDetail = {},
            modifier = Modifier,
        )
    }
}
