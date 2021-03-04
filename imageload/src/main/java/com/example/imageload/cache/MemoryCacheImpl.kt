package com.example.imageload.cache

import android.graphics.Bitmap

class MemoryCacheImpl():MemoryCache{
    override val size: Int
        get() = TODO("Not yet implemented")
    override val maxSize: Long
        get() = Runtime.getRuntime().maxMemory()

    override fun get(key: Long): Bitmap? {
        var bitmap = LruCache[key]
        if (bitmap == null) {
            bitmap = WeakCache[key]
        }
        return bitmap
    }

    override fun set(key: Long, bitmap: Bitmap) {
//        if (toWeakCache) {
//            WeakCache.put(key, bitmap)
//        } else {
            LruCache.put(key, bitmap)
//        }
    }

}