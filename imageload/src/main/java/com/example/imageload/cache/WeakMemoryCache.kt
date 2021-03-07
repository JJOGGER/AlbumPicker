package com.example.imageload.cache

import android.graphics.Bitmap

/**
 * 弱引用缓存
 */
interface WeakMemoryCache : BaseCacheService {
    fun remove(bitmap: Bitmap): Boolean
}