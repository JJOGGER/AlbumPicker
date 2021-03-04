package com.example.imageload.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import com.example.imageload.cache.ImageContentProvider
import java.io.File
import java.io.IOException

fun getBytesCount(bitmap: Bitmap?): Int {
    return if (bitmap != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bitmap.allocationByteCount
        } else {
            bitmap.rowBytes * bitmap.height
        }
    } else 0
}

internal val context: Context by lazy {
    val ctx = ImageContentProvider.ctx.applicationContext
    ctx
}

val cacheDir: String by lazy {
    context.cacheDir?.path
        ?: ("/data/" + "data/" + context.packageName + "/cache")
}

@Throws(IOException::class)
fun makeFileIfNotExist(file: File?): Boolean {
    return when {
        file == null -> false
        file.isFile -> true
        else -> {
            val parent = file.parentFile
            parent != null && (parent.isDirectory || parent.mkdirs()) && file.createNewFile()
        }
    }
}

fun hex2Long(hex: String): Long {
    fun byte2Int(b: Byte): Int {
        val ZERO = '0'.toByte()
        val NINE = '9'.toByte()
        val A = 'a'.toByte()
        val F = 'f'.toByte()
        return when (b) {
            in ZERO..NINE -> b - ZERO
            in A..F -> b - A + 10
            else -> throw NumberFormatException("invalid hex number")
        }
    }

    val buf = hex.toByteArray()
    if (buf.size != 16) {
        throw NumberFormatException("invalid long")
    }
    var a: Long = 0
    for (i in 0..7) {
        val index = i shl 1
        val b = (byte2Int(buf[index]) shl 4) or byte2Int(buf[index + 1])
        a = a shl 8
        a = a or b.toLong()
    }
    return a
}