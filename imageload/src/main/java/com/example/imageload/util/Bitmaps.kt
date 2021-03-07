package com.example.imageload.util

import android.graphics.Bitmap

internal val Bitmap.Config?.bytesPerPixel: Int
    get() = when {
        this == Bitmap.Config.ALPHA_8 -> 1
        this == Bitmap.Config.RGB_565 -> 2
        this == Bitmap.Config.ARGB_4444 -> 2
        android.os.Build.VERSION.SDK_INT >= 26 && this == Bitmap.Config.RGBA_F16 -> 8
        else -> 4
    }
internal val Bitmap.allocationByteCountCompat: Int
    get() {
        check(!isRecycled) { "Cannot obtain size for recycled bitmap: $this [$width x $height] + $config" }

        return try {
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                allocationByteCount
            } else {
                rowBytes * height
            }
        } catch (_: Exception) {
            calculateAllocationByteCount(width, height, config)
        }
    }