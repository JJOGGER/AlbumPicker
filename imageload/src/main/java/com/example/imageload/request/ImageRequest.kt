@file:Suppress("unused")

package com.example.imageload.request

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.example.imageload.R
import com.example.imageload.cache.constant.DiskCacheStrategy
import com.example.imageload.cache.constant.MemoryCacheStrategy
import com.example.imageload.decode.Decoder
import com.example.imageload.util.HashUtil

/**
 * 图片加载请求
 *
 */
class ImageRequest private constructor(
    val context: Context,
    val target: ImageView?,
    var data: Any?,
    var viewWidth: Int = 0,
    var viewHeight: Int = 0,
    @DrawableRes var placeholderResId: Int,
    var placeholderDrawable: Drawable?,
    @DrawableRes var errorResId: Int,
    var errorDrawable: Drawable?,
    var memoryCacheStrategy: Int,
    var diskCacheStrategy: Int,
    var clipType: Int,
    var keepOriginal: Boolean,//是否保留原图
    var config: Bitmap.Config
) {
    internal val key: Long by lazy { HashUtil.hash64(toString()) }

    internal var result: Bitmap? = null

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append("path:").append(data)
        builder.append(" size:").append(viewWidth)
            .append('x').append(viewHeight)
            .append(" type:").append(clipType)
            .append(" config:").append(config)
        return builder.toString()
    }

    class Builder(private val context: Context) {
        private var target: ImageView?
        private var data: Any?
        private var diskCacheStrategy: Int //磁盘缓存
        private var memoryCacheStrategy: Int //内存缓存

        @DrawableRes
        private var placeholderResId: Int
        private var placeholderDrawable: Drawable?

        @DrawableRes
        private var errorResId: Int
        private var errorDrawable: Drawable?
        private var viewWidth: Int
        private var viewHeight: Int
        private var clipType: Int
        private var keepOriginal: Boolean
        private var config: Bitmap.Config

        init {
            target = null
            data = null
            diskCacheStrategy = DiskCacheStrategy.RESULT
            memoryCacheStrategy = MemoryCacheStrategy.LRU
            placeholderResId = R.drawable.loader_default
            placeholderDrawable = null
            errorResId = -1
            errorDrawable = null
            viewWidth = 0
            viewHeight = 0
            clipType = Decoder.NO_CLIP
            keepOriginal = false
            config = Bitmap.Config.ARGB_8888
        }

        fun build(): ImageRequest {
            return ImageRequest(
                context = context,
                target = target,
                data = data,
                viewWidth = viewWidth,
                viewHeight = viewHeight,
                placeholderResId = placeholderResId,
                placeholderDrawable = placeholderDrawable,
                errorResId = errorResId,
                errorDrawable = errorDrawable,
                memoryCacheStrategy = memoryCacheStrategy,
                diskCacheStrategy = diskCacheStrategy,
                clipType = clipType,
                keepOriginal = keepOriginal,
                config = config
            )
        }

        fun data(data: Any?) = apply {
            this.data = data
        }

        fun target(imageView: ImageView) = apply {
            this.target = imageView
            this.clipType = Decoder.mapScaleType(imageView.scaleType)
        }

        /**
         * 图片宽高
         */
        fun override(width: Int, height: Int) = apply {
            this.viewWidth = width
            this.viewHeight = height
        }

        fun scaleType(scaleType: ImageView.ScaleType) = apply {
            this.clipType = Decoder.mapScaleType(scaleType)
        }

        /**
         * 内存缓存策略
         * @see [MemoryCacheStrategy]
         */
        fun memoryCacheStrategy(strategy: Int) = apply {
            this.memoryCacheStrategy = strategy
        }

        /**
         * 磁盘缓存策略
         * @see DiskCacheStrategy
         */
        fun diskCacheStrategy(strategy: Int) = apply {
            this.diskCacheStrategy = strategy
        }

        /**
         * 不使用缓存
         */
        fun noCache() = apply {
            this.memoryCacheStrategy = MemoryCacheStrategy.NONE
            this.diskCacheStrategy = DiskCacheStrategy.NONE
        }

        /**
         * 保留原图
         */
        fun keepOriginal() = apply {
            this.keepOriginal = true
        }

        fun placeholder(placeholderResId: Int) = apply {
            this.placeholderResId = placeholderResId
        }

        fun placeholder(drawable: Drawable) = apply {
            this.placeholderDrawable = drawable
        }

        fun error(errorResId: Int) = apply {
            this.errorResId = errorResId
        }

        fun error(drawable: Drawable) = apply {
            this.errorDrawable = drawable
        }

        /**
         * bitmap配置
         */
        fun config(config: Bitmap.Config) = apply {
            this.config = config
        }
    }
}
