package com.example.imageload.cache

import android.graphics.Bitmap

 interface DiskCache {
    fun set(key: Long, bitmap: Bitmap)

    fun get(key: Long):Bitmap?
}