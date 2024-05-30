package dev.veryniche.quickqr.previews

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItem
import dev.veryniche.quickqr.core.Constants.sampleQRCodeItemLongText
import dev.veryniche.quickqr.core.model.QRCodeItem

class QRCodeItemParameterProvider : PreviewParameterProvider<QRCodeItem> {
    override val values = sequenceOf(
        sampleQRCodeItem,
        sampleQRCodeItemLongText
//        sampleQRCodeItem.copy(name = "Very very very very very long name"),
//        sampleQRCodeItem.copy(content = "Very very very very very long content"),
//        sampleQRCodeItem.copy(
//            name = "Very very very very very long name",
//            content = "Very very very very very long content"
//        ),
    )
}