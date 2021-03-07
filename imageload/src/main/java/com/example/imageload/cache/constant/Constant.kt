package com.example.imageload.cache.constant

import android.os.Environment
import com.example.imageload.util.context

internal object Constant {
    val cacheDir: String by lazy {
        if ((Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) && context.externalCacheDir != null
        ) {
            context.externalCacheDir!!.path
        } else {
            context.cacheDir?.path
                ?: ("/data/" + "data/" + context.packageName + "/cache")
        }
    }
}