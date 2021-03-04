package com.example.imageload.cache


import android.graphics.Bitmap
import com.example.imageload.util.getBytesCount

internal class BitmapWrapper(var bitmap: Bitmap) {
    var bytesCount: Int = 0

    init {
        this.bytesCount = getBytesCount(bitmap)
    }
}

