package dev.veryniche.quickqr.core.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import dev.veryniche.quickqr.core.decodeImage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.format.DateTimeFormatter
import java.util.Date

@Serializer(forClass = Date::class)
class DateSerializer : KSerializer<Date> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: Date) {
        encoder.encodeLong(value.time)
    }

    override fun deserialize(decoder: Decoder): Date {
        return Date(decoder.decodeLong())
    }
}

object ColorAsStringSerializer : KSerializer<Color> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Color", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Color) {
        encoder.encodeLong(value.value.toLong())
    }

    override fun deserialize(decoder: Decoder): Color {
        return Color(decoder.decodeLong())
    }
}

@Serializable
data class QRCodeItem(
    val id: Int,
    val name: String,
    val content: String,
    val imageBase64: String,
    val icon: QRIcon,
    val primaryColor: QRColor,
    val sortOrder: Int,
    @Serializable(with = DateSerializer::class)
    val lastModified: Date
) {
    val imageBitmap = imageBase64.decodeImage().asImageBitmap()

    @Serializable(with = ColorAsStringSerializer::class)
    val secondaryColor = primaryColor.getSecondaryColor()
}


