//package com.example.imageload.cache
//
//import android.content.ComponentCallbacks2
//import android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND
//import android.content.ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW
//import android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN
//import android.graphics.Bitmap
//import android.renderscript.Sampler
//import android.util.Log
//import androidx.collection.LruCache
//import java.util.logging.Logger
//
///** An in-memory cache that holds strong references [Bitmap]s. */
//internal interface StrongMemoryCache {
//
//    companion object {
//        operator fun invoke(
//            maxSize: Int
//        ): StrongMemoryCache {
//            return  StrongMemoryCacheImpl(maxSize)
//
//        }
//    }
//
//    /** The current size of the memory cache in bytes. */
//    val size: Int
//
//    /** The maximum size of the memory cache in bytes. */
//    val maxSize: Int
//
//    /** Get the value associated with [key]. */
//    fun get(key: Long): Sampler.Value?
//
//    /** Set the value associated with [key]. */
//    fun set(key: Long, bitmap: Bitmap, isSampled: Boolean)
//
//    /** Remove the value referenced by [key] from this cache. */
//    fun remove(key: Long): Boolean
//
//    /** Remove all values from this cache. */
//    fun clearMemory()
//
//    /** @see ComponentCallbacks2.onTrimMemory */
//    fun trimMemory(level: Int)
//}
//
///** A [StrongMemoryCache] implementation backed by an [LruCache]. */
//private class StrongMemoryCacheImpl(
//    maxSize: Int
////    private val logger: Logger?
//) : StrongMemoryCache {
//
//    private val cache = object : LruCache<Long, InternalValue>(maxSize) {
//        override fun entryRemoved(
//            evicted: Boolean,
//            key: Long,
//            oldValue: InternalValue,
//            newValue: InternalValue?
//        ) {
////            val isPooled = referenceCounter.decrement(oldValue.bitmap)
////            if (!isPooled) {
////                // Add the bitmap to the WeakMemoryCache if it wasn't just added to the BitmapPool.
////                weakMemoryCache.set(key, oldValue.bitmap, oldValue.isSampled, oldValue.size)
////            }
//        }
//
//        override fun sizeOf(key: Long, value: InternalValue) = value.size
//    }
//
//    override val size get() = cache.size()
//
//    override val maxSize get() = cache.maxSize()
//
//    @Synchronized
//    override fun get(key: Long) = cache.get(key)
//
//    @Synchronized
//    override fun set(key: Long, bitmap: Bitmap, isSampled: Boolean) {
////        // If the bitmap is too big for the cache, don't even attempt to store it. Doing so will cause
////        // the cache to be cleared. Instead just evict an existing element with the same key if it exists.
////        val size = bitmap.allocationByteCountCompat
////        if (size > maxSize) {
////            val previous = cache.remove(key)
////            if (previous == null) {
////                // If previous != null, the value was already added to the weak memory cache in LruCache.entryRemoved.
////                weakMemoryCache.set(key, bitmap, isSampled, size)
////            }
////            return
////        }
////
////        referenceCounter.increment(bitmap)
////        cache.put(key, InternalValue(bitmap, isSampled, size))
//    }
//
//    @Synchronized
//    override fun remove(key: Long): Boolean {
//        return cache.remove(key) != null
//    }
//
//    @Synchronized
//    override fun clearMemory() {
//        cache.trimToSize(-1)
//    }
//
//    @Synchronized
//    override fun trimMemory(level: Int) {
//        if (level >= TRIM_MEMORY_BACKGROUND) {
//            clearMemory()
//        } else if (level in TRIM_MEMORY_RUNNING_LOW until TRIM_MEMORY_UI_HIDDEN) {
//            cache.trimToSize(size / 2)
//        }
//    }
//
////    private class InternalValue(
////        override val bitmap: Bitmap,
////        override val isSampled: Boolean,
////        val size: Int
////    ) : Sampler.Value
//
//    companion object {
//        private const val TAG = "RealStrongMemoryCache"
//    }
//}
