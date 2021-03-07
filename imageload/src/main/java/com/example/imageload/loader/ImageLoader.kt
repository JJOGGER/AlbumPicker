package com.example.imageload.loader

import android.app.ActivityManager
import android.content.Context
import com.example.imageload.cache.DiskCache
import com.example.imageload.cache.MemoryCache
import com.example.imageload.cache.StrongMemoryCache
import com.example.imageload.cache.WeakMemoryCache
import com.example.imageload.cache.impl.DiskCacheImpl
import com.example.imageload.cache.impl.MemoryCacheImpl
import com.example.imageload.cache.impl.WeakMemoryCacheImpl
//import com.example.imageload.request.Disposable
import com.example.imageload.request.ImageRequest
import com.example.imageload.util.calculateAvailableMemorySize

interface ImageLoader {

    val memoryCache: MemoryCache

    val diskCache: DiskCache
    fun load(request: ImageRequest)

    class Builder {
        companion object {
            private const val LOW_MEMORY_MULTIPLIER = 0.15
            private const val STANDARD_MULTIPLIER = 0.2
        }

        private val context: Context
        private var memoryCache: MemoryCacheImpl?
        private var diskCache: DiskCacheImpl?
        private var availableMemoryPercentage: Double
        private var bitmapPoolPercentage: Double

        constructor(context: Context) {
            this.context = context.applicationContext
            memoryCache = null
            availableMemoryPercentage = getDefaultAvailableMemoryPercentage(context)
            bitmapPoolPercentage = getDefaultBitmapPoolPercentage()
             diskCache = null
        }

        fun build(): ImageLoader {
            val memoryCache = memoryCache ?: buildDefaultMemoryCache()
            val diskCache = diskCache ?: DiskCacheImpl()
            return ImageLoaderImpl(memoryCache = memoryCache, diskCache = diskCache)
        }

        private fun buildDefaultMemoryCache(): MemoryCacheImpl {
            val availableMemorySize =
                calculateAvailableMemorySize(context, availableMemoryPercentage)
            val bitmapPoolSize = (bitmapPoolPercentage * availableMemorySize).toInt()
            val memoryCacheSize = (availableMemorySize - bitmapPoolSize).toInt()
            val weakMemoryCacheImpl = WeakMemoryCacheImpl()
            val strongMemoryCache = StrongMemoryCache(weakMemoryCacheImpl, memoryCacheSize)
            return MemoryCacheImpl(strongMemoryCache, weakMemoryCacheImpl)
        }

        fun getDefaultBitmapPoolPercentage(): Double {
            return when {
                // Prefer immutable bitmaps (which cannot be pooled) on API 24 and greater.
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N -> 0.0
                // Bitmap pooling is most effective on APIs 19 to 23.
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT -> 0.5
                // The requirements for bitmap reuse are strict below API 19.
                else -> 0.25
            }
        }

        private fun getDefaultAvailableMemoryPercentage(context: Context): Double {
            return try {
                val activityManager: ActivityManager =
                    checkNotNull(context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager) { "System service of type ${ActivityManager::class.java} was not found." }
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT || activityManager.isLowRamDevice) LOW_MEMORY_MULTIPLIER else STANDARD_MULTIPLIER
            } catch (_: Exception) {
                STANDARD_MULTIPLIER
            }
        }
    }

    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke(context: Context) = Builder(context).build()
    }
}