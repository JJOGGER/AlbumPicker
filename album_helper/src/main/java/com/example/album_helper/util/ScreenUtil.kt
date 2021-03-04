package com.example.album_helper.util

import android.content.Context

fun deviceWidth(context: Context): Int {
    return try {
        val metrics = context.resources.displayMetrics
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels
        if (screenWidth > screenHeight) screenHeight else screenWidth
    } catch (var4: Exception) {
        var4.printStackTrace()
        0
    }
}