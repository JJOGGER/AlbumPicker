package com.example.imageload.loader

import android.graphics.Bitmap
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.example.imageload.coroutine.Coroutine
import com.example.imageload.cache.*
import com.example.imageload.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.lang.ref.WeakReference

/**
 * loader实现
 */
class ImageLoaderImpl(override val memoryCache: MemoryCacheImpl) : ImageLoader {
    private var fromDiskCache = false
    private var targetReference: WeakReference<ImageView>? = null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    override fun load(request: ImageRequest) {
        if (request.target == null)
            return
        targetReference = WeakReference(request.target!!)
        when {
            request.target!!.width > 0 && request.target!!.height > 0 -> {

            }
        }
        loadImage(request,request.target!!.width,request.target!!.height)

    }

    private fun loadImage(request: ImageRequest, targetWidth: Int, targetHeight: Int) {
        var target: ImageView? = null
        if (targetReference != null) {
            target = targetReference!!.get()
            target?.let {
                if (checkTag(request, it))
                    return@let
                it.tag = null
            }
        }
        when(request.data){
            is String->{
                if (TextUtils.isEmpty(request.data))
                    abort(request, target)
            }
        }

        var bitmap = memoryCache.get(request.key)
        val waiter = request.waiter
        if (waiter != null && (bitmap != null || waiter.timeout == 0L)) {
            waiter.result = bitmap
            return
        }
        if (bitmap==null){
//                val filePath = DiskCache[request.key]
//                val fromDiskCache = !TextUtils.isEmpty(filePath)
//                val source = if (fromDiskCache) Source.valueOf(File(filePath!!)) else Source.parse(request)
//                val gifDecoder = Config.gifDecoder
////                if (!fromDiskCache && request.gifPriority && gifDecoder != null
////                    && HeaderParser.isGif(source.magic)) {
////                    return gifDecoder.decode(source.data)
////                }
//                bitmap = Decoder.decode(source, request, fromDiskCache)
//                bitmap = transform(request, bitmap)
//                if (bitmap != null) {
//                    if (request.memoryCacheStrategy != MemoryCacheStrategy.NONE) {
//                        val toWeakCache = request.memoryCacheStrategy == MemoryCacheStrategy.WEAK
//                        memoryCache.set(request.key, bitmap!!, toWeakCache)
//                    }
//                    if (!fromDiskCache && request.diskCacheStrategy and DiskCacheStrategy.RESULT != 0) {
//                        DiskCache.put(request.key, bitmap!!)
//                    }
//                }
                LoadHelper.start(request,memoryCache)

        }
    }
    private fun transform(request: ImageRequest, source: Bitmap?): Bitmap? {
        var output = source
        if (output != null && !fromDiskCache && !request.transformations.isNullOrEmpty()) {
            for (transformation in request.transformations!!) {
                output = transformation.transform(output!!)
                if (output == null) {
                    break
                }
            }
        }
        return output
    }
    private fun abort(request: ImageRequest, imageView: ImageView?) {
//        if (request.simpleTarget != null) {
//            request.simpleTarget!!.onComplete(null)
//        } else if (imageView != null) {
//            if (request.callback != null && request.callback!!.onReady(null)) {
//                return
//            }
//            if (request.goneIfMiss || request.errorResId >= 0 || request.errorDrawable != null) {
//                setError(request, imageView)
//            } else {
//                setPlaceholder(request, imageView)
//            }
//        }
    }

    private fun setPlaceholder(request: ImageRequest, imageView: ImageView) {
        if (request.placeholderDrawable != null) {
            imageView.setImageDrawable(request.placeholderDrawable)
        } else if (request.placeholderResId >= 0) {
            imageView.setImageResource(request.placeholderResId)
        }
    }

    private fun setError(request: ImageRequest, imageView: ImageView) {
        when {
            request.goneIfMiss -> imageView.visibility = View.GONE
            request.errorDrawable != null -> imageView.setImageDrawable(request.errorDrawable)
            request.errorResId >= 0 -> imageView.setImageResource(request.errorResId)
        }
    }

    private fun checkTag(request: ImageRequest, imageView: ImageView): Boolean {
        val tag = imageView.tag
//        if (tag is ImageRequest) {
//            if (request.key == tag.key) {
//                return true
//            } else {
//                val preTask = tag.workerReference!!.get()
//                if (preTask != null && !preTask.isCancelled) {
//                    preTask.cancel(false)
//                }
//            }
//        } else if (tag != null) {
//            val e =
//                IllegalArgumentException("Don't call setTag() on a view Doodle is targeting, try setTag(int, Object)")
//            return true
//            // shell we throw the exception ?
//            // throw e;
//        }
        return false
    }


}