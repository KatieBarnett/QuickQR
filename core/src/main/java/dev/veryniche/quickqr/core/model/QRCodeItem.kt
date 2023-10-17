package dev.veryniche.quickqr.core.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class QRCodeItem(
    val name: String,
    val content: String,
    val imageBase64: String,
    val icon: ImageVector,
    val primaryColor: Color,
    val secondaryColor: Color,
)