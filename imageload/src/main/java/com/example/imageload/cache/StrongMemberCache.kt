package com.example.imageload.cache

import com.example.imageload.cache.impl.StrongMemoryCacheImpl

/**
 * 内存缓存
 */
internal interface StrongMemoryCache : BaseCacheService {
    val size: Int

    val maxSize: Int

    companion object {
        operator fun invoke(
            weakMemoryCache: WeakMemoryCache,
            maxSize: Int
        ): StrongMemoryCache {
            return if (maxSize <= 0) {
                StrongMemoryCacheImpl(weakMemoryCache, 1)
            } else
                StrongMemoryCacheImpl(weakMemoryCache, maxSize)
        }
    }

}