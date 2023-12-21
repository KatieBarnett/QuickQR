package dev.veryniche.quickqr.storage.util

import dev.veryniche.quickqr.core.model.QRCodeItem
import dev.veryniche.quickqr.core.model.QRColor
import dev.veryniche.quickqr.core.model.QRIcon
import dev.veryniche.quickqr.storage.models.QRCode
import java.lang.IllegalArgumentException
import java.util.Date

fun QRCode.toQRCodeItem(): QRCodeItem {
    return QRCodeItem(
        id = id,
        name = name,
        content = content,
        imageBase64 = imageBase64,
        icon = try {
            QRIcon.valueOf(iconName)
        } catch (e: IllegalArgumentException) {
            QRIcon.ADD_QR_CODE
        },
        primaryColor = try {
            QRColor.valueOf(colorName)
        } catch (e: IllegalArgumentException) {
            QRColor.Primary
        },
        sortOrder = sortOrder,
        lastModified = Date(lastModified.toLong())
    )
}

fun QRCodeItem.toQRCode(newId: Int? = null): QRCode {
    val builder = QRCode.newBuilder()
    builder.id = newId ?: id ?: 0
    builder.name = name
    builder.content = content
    builder.imageBase64 = imageBase64
    builder.iconName = icon.name
    builder.colorName = primaryColor.name
    builder.sortOrder = sortOrder ?: -1
    builder.lastModified = lastModified.time.toDouble()
    return builder.build()
}
