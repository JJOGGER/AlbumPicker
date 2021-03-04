package com.example.imageload.cache

object MemoryCacheStrategy{
    /**
     * Saves no data to memory cache
     */
    const val NONE = 0
    /**
     * Just save [WeakCache]
     */
    const val WEAK = 1
    /**
     * Save to [LruCache] first.
     * when [LruCache] is out of capacity, some of them will move to [WeakCache]
     */
    const val LRU = 2
}