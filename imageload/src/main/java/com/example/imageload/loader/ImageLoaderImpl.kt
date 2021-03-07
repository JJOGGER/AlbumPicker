package com.example.imageload.loader

import android.app.Activity
import android.graphics.Bitmap
import android.os.Build
import android.text.TextUtils
import android.widget.ImageView
import com.example.imageload.cache.DiskCache
import com.example.imageload.cache.MemoryCache
import com.example.imageload.cache.constant.DiskCacheStrategy
import com.example.imageload.cache.constant.MemoryCacheStrategy
import com.example.imageload.coroutine.Coroutine
import com.example.imageload.decode.Decoder
import com.example.imageload.decode.Source
import com.example.imageload.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import java.lang.ref.WeakReference

/**
 * loader实现
 */
class ImageLoaderImpl(override val memoryCache: MemoryCache, override val diskCache: DiskCache) :
    ImageLoader {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var targetReference: WeakReference<ImageView>? = null
    override fun load(request: ImageRequest) {
        if (request.target == null)
            return
        targetReference = WeakReference(request.target)
        if (!prepareStartBefore(request))
            return
        val bitmap = memoryCache.get(request.key) //获取缓存数据
        if (request.result != null && bitmap != null) {
            request.result = bitmap
            return
        }
        if (bitmap != null) {
            handleloadAfter(request, bitmap)
        } else {
            Coroutine.async(scope) {
                if (isViewDestroyed(request)) {
                    cancel()
                }
                request.target.tag = request
                val result = start(request)
                result
            }.onSuccess {
                if (isViewDestroyed(request)) {
                    cancel()
                    return@onSuccess
                }
                handleloadAfter(request, it)
            }
        }
    }

    private fun start(request: ImageRequest): Bitmap? {
        var bitmap: Bitmap? = diskCache.get(request.key)
        if (bitmap != null) {
            if (request.memoryCacheStrategy != MemoryCacheStrategy.NONE) {
                val toWeakCache = request.memoryCacheStrategy == MemoryCacheStrategy.WEAK
                memoryCache.set(request.key, bitmap, toWeakCache)
            }
            return bitmap
        }
        val source = Source.parse(request)
        bitmap = Decoder.decode(source, request)
        if (bitmap != null) {
            if (request.memoryCacheStrategy != MemoryCacheStrategy.NONE) {
                val toWeakCache = request.memoryCacheStrategy == MemoryCacheStrategy.WEAK
                memoryCache.set(request.key, bitmap, toWeakCache)
            }
            if (request.diskCacheStrategy and DiskCacheStrategy.RESULT != 0) {//当设置存所有或者存结果时，做存储
                diskCache.set(request.key, bitmap)
            }
        }
        return bitmap
    }

    private fun isViewDestroyed(
        request: ImageRequest
    ): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            request.context is Activity && (request.context.isDestroyed || request.context.isFinishing)
        } else {
            request.context is Activity && (request.context.isFinishing)
        }
    }

    /**
     *开开始前的一些操作
     */
    private fun prepareStartBefore(request: ImageRequest): Boolean {
        request.run {
            if (target == null) return false
            if (checkTag(request, target))
                return false
            target.tag = null
            if (data == null) {
                exceptionLoad(request, target)
                return false
            }
            when (data) {
                is String -> {
                    val dataString = data as String
                    if (TextUtils.isEmpty(dataString)) {
                        exceptionLoad(this, target)
                        return false
                    }
                    data =
                        if (dataString.startsWith("http") || dataString.contains("://")) dataString else "file://$dataString"
                }
            }
            when {
                keepOriginal -> {
                    clipType = Decoder.NO_CLIP
                    viewWidth = 0
                    viewHeight = 0
                }
                viewWidth > 0 && viewHeight > 0 -> {

                }
                target.width > 0 && target.height > 0 -> {
                    viewWidth = target.width
                    viewHeight = target.height
                }
                else -> {
                    viewWidth = 0
                    viewHeight = 0
                }

            }
            //先设置占位图
            if (placeholderDrawable != null) {
                target.setImageDrawable(placeholderDrawable)
            } else {
                target.setImageResource(placeholderResId)
            }
        }

        return true

    }

    /**
     * 加载结束后的处理
     */
    private fun handleloadAfter(
        request: ImageRequest,
        bitmap: Bitmap?
    ) {
        if (request.result != null) {
            return
        }
        if (bitmap == null)
            return
        request.result = bitmap
        request.target?.setImageBitmap(bitmap)

    }

    /**
     * 沒有数据，设置默认图片资源
     */
    private fun exceptionLoad(request: ImageRequest, target: ImageView?) {
        target?.let {
            when {
                request.placeholderDrawable != null -> it.setImageDrawable(request.placeholderDrawable)
                request.placeholderResId >= 0 -> it.setImageResource(request.placeholderResId)
                request.errorDrawable != null -> it.setImageDrawable(request.errorDrawable)
                request.errorResId >= 0 -> it.setImageResource(request.errorResId)
            }
        }

    }

    private fun checkTag(request: ImageRequest, imageView: ImageView): Boolean {
        val tag = imageView.tag
        if (tag is ImageRequest) {
            if (request.key == tag.key) {
                return true
            }
        }
        return false
    }


}