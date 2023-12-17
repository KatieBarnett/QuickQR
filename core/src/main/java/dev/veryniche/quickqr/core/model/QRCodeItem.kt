package dev.veryniche.quickqr.core.model

import androidx.compose.ui.graphics.asImageBitmap
import dev.veryniche.quickqr.core.decodeImage
import java.util.Date

data class QRCodeItem(
    val id: Int,
    val name: String,
    val content: String,
    val imageBase64: String,
    val icon: QRIcon,
    val primaryColor: QRColor,
    val sortOrder: Int? = null,
    val lastModified: Date
) {
    val imageBitmap = imageBase64.decodeImage().asImageBitmap()
    val secondaryColor = primaryColor.getSecondaryColor()
}


