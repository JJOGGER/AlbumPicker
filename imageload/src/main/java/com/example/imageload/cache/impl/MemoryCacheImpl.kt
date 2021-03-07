package com.example.imageload.cache.impl

import android.graphics.Bitmap
import com.example.imageload.cache.*
import com.example.imageload.cache.StrongMemoryCache

internal class MemoryCacheImpl(
    val strongMemoryCache: StrongMemoryCache,
    val weakMemoryCache: WeakMemoryCache
) : MemoryCache {
    override val size: Int
        get() = strongMemoryCache.size
    override val maxSize
        get() = strongMemoryCache.maxSize

    override fun get(key: Long): Bitmap? {
        //先取lru，再取弱引用
        return strongMemoryCache.get(key) ?: weakMemoryCache.get(key)
    }

    override fun set(key: Long, bitmap: Bitmap) {
        set(key, bitmap, false)
    }

    override fun set(key: Long, bitmap: Bitmap, onlyWeak: Boolean) {
        if (onlyWeak) {
            weakMemoryCache.set(key, bitmap) // 移除已存在的弱引用
        } else {
            strongMemoryCache.set(key, bitmap)
            weakMemoryCache.remove(key) // 移除已存在的弱引用
        }
    }

    override fun remove(key: Long): Boolean {
        val removedStrong = strongMemoryCache.remove(key)
        val removedWeak = weakMemoryCache.remove(key)
        return removedStrong || removedWeak
    }

    override fun clear() {
        strongMemoryCache.clearMemory()
        weakMemoryCache.clearMemory()
    }

}