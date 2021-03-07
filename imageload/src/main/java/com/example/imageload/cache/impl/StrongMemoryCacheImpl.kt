package com.example.imageload.cache.impl

import android.content.ComponentCallbacks2.*
import android.graphics.Bitmap
import androidx.collection.LruCache
import com.example.imageload.cache.StrongMemoryCache
import com.example.imageload.cache.WeakMemoryCache
import com.example.imageload.util.allocationByteCountCompat

internal class StrongMemoryCacheImpl(private val weakMemoryCache: WeakMemoryCache, maxSize: Int) :
    StrongMemoryCache {
    private val cache = object : LruCache<Long, Bitmap>(maxSize) {

    }
    override val size: Int
        get() = cache.size()
    override val maxSize: Int
        get() = cache.maxSize()

    override fun get(key: Long): Bitmap? =
        cache.get(key)

    override fun set(key: Long, bitmap: Bitmap) {
        val size = bitmap.allocationByteCountCompat
        if (size > maxSize) {//如果超出了lru的最大字节数，则使用弱引用
            val previous = cache.remove(key)
            if (previous == null) {
                weakMemoryCache.set(key, bitmap)
            }
            return
        }

        cache.put(key, bitmap)
    }

    override fun remove(key: Long): Boolean = cache.remove(key) != null

    override fun clearMemory() {
        cache.trimToSize(-1)
    }

    override fun trimMemory(level: Int) {
        if (level >= TRIM_MEMORY_BACKGROUND) {
            clearMemory()
        } else if (level in TRIM_MEMORY_RUNNING_LOW until TRIM_MEMORY_UI_HIDDEN) {
            cache.trimToSize(size / 2)
        }
    }
}