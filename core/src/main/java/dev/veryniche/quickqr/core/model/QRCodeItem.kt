package dev.veryniche.quickqr.core.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.util.Date

data class QRCodeItem(
    val id: Int,
    val name: String,
    val content: String,
    val imageBase64: String,
    val icon: Icon,
    val primaryColor: Color,
    val secondaryColor: Color,
    val sortOrder: Int? = null,
    val lastModified: Date
) {
    val primaryColorInt = primaryColor.toArgb()
    val secondaryColorInt = secondaryColor.toArgb()
}

