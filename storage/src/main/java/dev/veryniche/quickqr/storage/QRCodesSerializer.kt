package dev.veryniche.quickqr.storage

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import dev.veryniche.quickqr.storage.models.Qrcodes
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class QrCodesSerializer @Inject constructor() : Serializer<Qrcodes> {

    override val defaultValue: Qrcodes = Qrcodes.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): Qrcodes {
        try {
            // readFrom is already called on the data store background thread
            @Suppress("BlockingMethodInNonBlockingContext")
            return Qrcodes.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: Qrcodes, output: OutputStream) =
        // writeTo is already called on the data store background thread
        @Suppress("BlockingMethodInNonBlockingContext")
        t.writeTo(output)

}