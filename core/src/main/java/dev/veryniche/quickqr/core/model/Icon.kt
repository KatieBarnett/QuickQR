package dev.veryniche.quickqr.core.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Web
import androidx.compose.ui.graphics.vector.ImageVector

enum class Icon(val vector: ImageVector, val showInList: Boolean = true) {
    ADD_QR_CODE(Icons.Rounded.QrCode2, showInList = false),
    WEB(Icons.Rounded.Web),
    STAR(Icons.Rounded.Star)
}