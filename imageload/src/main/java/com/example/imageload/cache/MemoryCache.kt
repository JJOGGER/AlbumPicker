package com.example.imageload.cache

import android.graphics.Bitmap

/**
 * 缓存管理
 * 1.内存缓存 LruCache
 * 2.弱引用
 */
interface MemoryCache {
    val size: Int
    val maxSize: Int

    operator fun get(key: Long): Bitmap?

    operator fun set(key: Long, bitmap: Bitmap)

    /**
     * 只保存弱引用
     */
    operator fun set(key: Long, bitmap: Bitmap, onlyWeak: Boolean)

    fun remove(key: Long): Boolean

    fun clear()

}