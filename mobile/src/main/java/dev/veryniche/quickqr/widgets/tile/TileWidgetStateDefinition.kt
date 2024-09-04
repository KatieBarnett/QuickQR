package dev.veryniche.quickqr.widgets.tile

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.datastore.preferences.preferencesDataStore
import androidx.glance.state.GlanceStateDefinition
import dev.veryniche.quickqr.core.model.QRCodeItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream
import java.io.OutputStream

/**
 * Provides our own definition of "Glance state" using Kotlin serialization.
 */
object TileWidgetStateDefinition : GlanceStateDefinition<TileWidgetState?> {

    private const val DATA_STORE_FILENAME = "tileWidgetState"

    /**
     * Use the same file name regardless of the widget instance to share data between them
     *
     * If you need different state/data for each instance, create a store using the provided fileKey
     */
    private val Context.datastore by dataStore(DATA_STORE_FILENAME, TileWidgetStateSerializer)

    override suspend fun getDataStore(context: Context, fileKey: String): DataStore<TileWidgetState?> {
        return context.datastore
    }

    override fun getLocation(context: Context, fileKey: String): File {
        return context.dataStoreFile(DATA_STORE_FILENAME)
    }

    object TileWidgetStateSerializer : Serializer<TileWidgetState?> {
        override val defaultValue = null

        override suspend fun readFrom(input: InputStream): TileWidgetState? = try {
            Json.decodeFromString<TileWidgetState>(
                input.readBytes().decodeToString()
            )
        } catch (exception: SerializationException) {
            throw CorruptionException("Could not read data: ${exception.message}")
        }

        override suspend fun writeTo(t: TileWidgetState?, output: OutputStream) {
            output.use {
                it.write(
                    Json.encodeToString(t).encodeToByteArray()
                )
            }
        }
    }
}

@Serializable
data class TileWidgetState(
    val tile: QRCodeItem,
    val useColoredBackground: Boolean
)
