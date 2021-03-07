package com.example.imageload.util

import android.app.ActivityManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.Px
import com.example.imageload.ImageContentProvider
import java.io.Closeable
import java.io.File
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.text.StringBuilder

fun getBytesCount(bitmap: Bitmap?): Int {
    return if (bitmap != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bitmap.allocationByteCount
        } else {
            bitmap.rowBytes * bitmap.height
        }
    } else 0
}

internal val context: Context by lazy {
    val ctx = ImageContentProvider.ctx.applicationContext
    ctx
}

val cacheDir: String by lazy {
    context.cacheDir?.path
        ?: ("/data/" + "data/" + context.packageName + "/cache")
}

fun closeQuietly(closeable: Closeable?) {
    if (closeable != null) {
        try {
            closeable.close()
        } catch (e: IOException) {
            // ignore
        }
    }
}

@Throws(IOException::class)
fun makeFileIfNotExist(file: File?): Boolean {
    return when {
        file == null -> false
        file.isFile -> true
        else -> {
            val parent = file.parentFile
            parent != null && (parent.isDirectory || parent.mkdirs()) && file.createNewFile()
        }
    }
}

fun bytes2Hex(bytes: ByteArray) {
    val stringBuilder = StringBuilder()
    for (b in bytes) {
        long2Hex((0xFF and b.toInt()).toLong())
    }
}

internal inline fun String.md5(): String {
    try {
        val instance: MessageDigest = MessageDigest.getInstance("MD5")//获取md5加密对象
        val digest: ByteArray = instance.digest(toByteArray())//对字符串加密，返回字节数组
        val sb = StringBuffer()
        for (b in digest) {
            val i: Int = b.toInt() and 0xff//获取低八位有效值
            var hexString = Integer.toHexString(i)//将整数转化为16进制
            if (hexString.length < 2) {
                hexString = "0" + hexString//如果是一位的话，补0
            }
            sb.append(hexString)
        }
        return sb.toString()

    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }

    return ""
}

private val HEX_DIGITS =
    charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

fun long2Hex(num: Long): String {
    var a = num
    val buf = CharArray(16)
    for (i in 7 downTo 0) {
        val index = i shl 1
        val b = (a and 0xFF).toInt()
        buf[index] = HEX_DIGITS[b and 0xF0 shr 4]
        buf[index + 1] = HEX_DIGITS[b and 0xF]
        a = a.ushr(8)
    }
    return String(buf)
}

fun hex2Long(hex: String): Long {
    fun byte2Int(b: Byte): Int {
        val ZERO = '0'.toByte()
        val NINE = '9'.toByte()
        val A = 'a'.toByte()
        val F = 'f'.toByte()
        return when (b) {
            in ZERO..NINE -> b - ZERO
            in A..F -> b - A + 10
            else -> throw NumberFormatException("invalid hex number")
        }
    }

    val buf = hex.toByteArray()
    if (buf.size != 16) {
        throw NumberFormatException("invalid long")
    }
    var a: Long = 0
    for (i in 0..7) {
        val index = i shl 1
        val b = (byte2Int(buf[index]) shl 4) or byte2Int(buf[index + 1])
        a = a shl 8
        a = a or b.toLong()
    }
    return a
}

/**
 * 根据宽高返回bitmap大小
 */
fun calculateAllocationByteCount(@Px width: Int, @Px height: Int, config: Bitmap.Config?): Int {
    return width * height * config.bytesPerPixel
}

//参照picasso计算比例
private const val DEFAULT_MEMORY_CLASS_MEGABYTES = 256
fun calculateAvailableMemorySize(context: Context, percentage: Double): Long {
    val memoryClassMegabytes = try {
        val activityManager: ActivityManager =
            checkNotNull(context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager) { "System service of type ${ActivityManager::class.java} was not found." }
        val isLargeHeap = (context.applicationInfo.flags and ApplicationInfo.FLAG_LARGE_HEAP) != 0
        if (isLargeHeap) activityManager.largeMemoryClass else activityManager.memoryClass
    } catch (_: Exception) {
        DEFAULT_MEMORY_CLASS_MEGABYTES
    }
    return (percentage * memoryClassMegabytes * 1024 * 1024).toLong()
}


fun appVersion(): Int {
    try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionCode
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return 1
}