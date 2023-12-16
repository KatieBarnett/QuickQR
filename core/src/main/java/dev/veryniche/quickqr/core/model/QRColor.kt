package dev.veryniche.quickqr.core.model

import androidx.compose.ui.graphics.Color

// Colours from https://materialui.co/metrocolors
enum class QRColor(val color: Color, val showInList: Boolean = true) {
    Lime(Color(0xFFA4C400)),
    Green(Color(0xFF60A917)),
    Emerald(Color(0xFF008A00)),
    Teal(Color(0xFF00ABA9)),
    Cyan(Color(0xFF1BA1E2)),
    Cobalt(Color(0xFF0050EF)),
    Indigo(Color(0xFF6A00FF)),
    Violet(Color(0xFFAA00FF)),
    Pink(Color(0xFFF472D0)),
    Magenta(Color(0xFFD80073)),
    Crimson(Color(0xFFA20025)),
    Red(Color(0xFFE51400)),
    Orange(Color(0xFFFA6800)),
    Amber(Color(0xFFF0A30A)),
    Yellow(Color(0xFFE3C800)),
    Brown(Color(0xFF825A2C)),
    Olive(Color(0xFF6D8764)),
    Steel(Color(0xFF647687)),
    Mauve(Color(0xFF76608A)),
    Sienna(Color(0xFFA0522D)),
    Clouds(Color(0xFFECF0F1)),
}

fun getRandomColor(): QRColor {
    return QRColor.entries.filter { it.showInList }.random()
}
