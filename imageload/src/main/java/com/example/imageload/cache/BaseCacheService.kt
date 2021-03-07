package com.example.imageload.cache

import android.graphics.Bitmap

interface BaseCacheService {
    fun get(key: Long): Bitmap?

    fun set(key: Long, bitmap: Bitmap)

    fun remove(key: Long): Boolean

    fun clearMemory()

    fun trimMemory(level: Int)
}