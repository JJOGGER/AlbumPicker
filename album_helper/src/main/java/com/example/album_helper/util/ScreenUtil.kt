package com.example.album_helper.util

import android.content.Context

import android.content.res.Resources
import android.graphics.Canvas
import kotlin.math.roundToInt

object ScreenUtil {

    private val DENSITY = Resources.getSystem().displayMetrics.density
    private val sCanvas = Canvas()

    fun dp2Px(dp: Int): Int {
        return (dp * DENSITY).roundToInt()
    }
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
}
