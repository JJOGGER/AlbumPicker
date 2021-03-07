package com.example.imageload.cache.impl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.imageload.cache.DiskCache
import com.example.imageload.cache.constant.Constant
import com.example.imageload.util.appVersion
import com.example.imageload.util.long2Hex
import com.example.imageload.util.md5
import com.jakewharton.disklrucache.DiskLruCache
import java.io.File


internal class DiskCacheImpl : DiskCache {
    var DEFAULT_DISK_CACHE_SIZE = 250 * 1024 * 1024.toLong()
    private lateinit var cachePath: String
    private val cache: DiskLruCache by lazy {
        cachePath = Constant.cacheDir + "/result/"
        DiskLruCache.open(
            File(cachePath),
            appVersion(),
            1,
            DEFAULT_DISK_CACHE_SIZE
        )
    }

    override fun set(key: Long, bitmap: Bitmap) {
        synchronized(this) {
            try {
                val name = keyToPath(key)

                val edit = cache.edit(name.md5())
                if (edit != null) {
                    val out = edit.newOutputStream(0)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
//                    closeQuietly(out)
                    edit.commit()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun keyToPath(key: Long): String {
        return cachePath + long2Hex(key)
    }

    override fun get(key: Long): Bitmap? {
        val snapshot = cache.get(keyToPath(key).md5())
        return if (snapshot != null) {
            BitmapFactory.decodeStream(snapshot.getInputStream(0))
        } else {
            null
        }
    }


}