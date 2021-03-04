package com.example.imageload.util

import android.graphics.Bitmap
import android.os.Build

fun getBytesCount(bitmap: Bitmap?): Int {
    return if (bitmap != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bitmap.allocationByteCount
        } else {
            bitmap.rowBytes * bitmap.height
        }
    } else 0
}