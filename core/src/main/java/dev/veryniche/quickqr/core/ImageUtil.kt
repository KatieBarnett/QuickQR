package dev.veryniche.quickqr.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun Bitmap.encodeImage(): String {
    val baos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val byteArray = baos.toByteArray()
    return Base64.encode(byteArray)
}

@OptIn(ExperimentalEncodingApi::class)
fun String.decodeImage(): Bitmap {
    val byteArray = Base64.decode(this)
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}