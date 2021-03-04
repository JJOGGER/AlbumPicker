package com.example.imageload.cache

import android.graphics.Bitmap

interface MemoryCache {
    val size:Int
    val maxSize:Long

    operator fun get(key:Long):Bitmap?

    operator fun set(key: Long,bitmap: Bitmap)

}