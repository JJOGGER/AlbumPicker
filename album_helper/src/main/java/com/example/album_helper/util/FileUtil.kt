package com.example.album_helper.util

import android.os.Environment
import android.text.TextUtils

fun isSDAvailable(): Boolean {
    var isAvailable = false
    try {
        isAvailable = TextUtils.equals(Environment.getExternalStorageState(), "mounted")
    } catch (var2: Throwable) {
        var2.printStackTrace()
    }
    return isAvailable
}