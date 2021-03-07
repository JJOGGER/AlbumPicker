package com.example.imageload.cache.impl

import android.content.ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW
import android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN
import android.graphics.Bitmap
import com.example.imageload.cache.WeakMemoryCache
import com.example.imageload.util.firstNotNullIndices
import com.example.imageload.util.removeIfIndices
import java.lang.ref.WeakReference

internal class WeakMemoryCacheImpl : WeakMemoryCache {
    private val cache = hashMapOf<Long, ArrayList<WeakValue>>()
    private var operationsSinceCleanUp = 0

    companion object {
        private const val CLEAN_UP_INTERVAL = 10
    }

    @Synchronized
    override fun get(key: Long): Bitmap? {
        val values = cache[key] ?: return null
        val bitmap = values.firstNotNullIndices { value ->
            value.bitmap.get()
        }
        cleanUpIfNecessary()
        return bitmap
    }

    override fun set(key: Long, bitmap: Bitmap) {
        val values = cache.getOrPut(key) { arrayListOf() }

        run {
            val identityHashCode = System.identityHashCode(bitmap)
            val newValue = WeakValue(identityHashCode, WeakReference(bitmap))
            for (index in values.indices) {
                val value = values[index]
                if (System.identityHashCode(value) == identityHashCode && value.bitmap.get() === bitmap) {
                    values[index] = newValue
                } else {
                    values.add(index, newValue)
                }
                return@run
            }
            values += newValue
        }

        cleanUpIfNecessary()
    }

    override fun remove(bitmap: Bitmap): Boolean {
        val identityHashCode = System.identityHashCode(bitmap)

        val removed = run {
            cache.values.forEach { values ->
                for (index in values.indices) {
                    if (values[index].identityHashCode == identityHashCode) {
                        values.removeAt(index)
                        return@run true
                    }
                }
            }
            return@run false
        }

        cleanUpIfNecessary()
        return removed
    }

    override fun remove(key: Long): Boolean {
        return cache.remove(key) != null
    }

    override fun clearMemory() {
        operationsSinceCleanUp = 0
        cache.clear()
    }

    override fun trimMemory(level: Int) {
        if (level >= TRIM_MEMORY_RUNNING_LOW && level != TRIM_MEMORY_UI_HIDDEN) {
            cleanUp()
        }
    }

    private fun cleanUpIfNecessary() {
        if (operationsSinceCleanUp++ >= CLEAN_UP_INTERVAL) {
            cleanUp()
        }
    }

    internal fun cleanUp() {
        operationsSinceCleanUp = 0

        val iterator = cache.values.iterator()
        while (iterator.hasNext()) {
            val list = iterator.next()

            if (list.count() <= 1) {
                if (list.firstOrNull()?.bitmap?.get() == null) {
                    iterator.remove()
                }
            } else {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    list.removeIf { it.bitmap.get() == null }
                } else {
                    list.removeIfIndices { it.bitmap.get() == null }
                }

                if (list.isEmpty()) {
                    iterator.remove()
                }
            }
        }
    }

    internal class WeakValue(
        val identityHashCode: Int,
        val bitmap: WeakReference<Bitmap>
    )
}