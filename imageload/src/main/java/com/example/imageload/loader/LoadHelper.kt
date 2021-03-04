package com.example.imageload.loader

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.*
import android.text.TextUtils
import android.util.SparseArray
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.album_helper.coroutine.Coroutine
import com.example.imageload.cache.*
import com.example.imageload.cache.DiskCache
import com.example.imageload.decode.Decoder
import com.example.imageload.decode.Source
import com.example.imageload.request.ImageRequest
import com.example.imageload.util.context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import java.io.File
import java.util.concurrent.TimeUnit


internal object LoadHelper:CoroutineScope by MainScope() {
    private const val TAG = "Dispatcher"
    private var fromDiskCache = false

    @Volatile
    private var pauseFlag = false
    private val pausedRequests = SparseArray<ImageRequest>()

    fun pause() {
        pauseFlag = true
    }

    @Synchronized
    private fun pause(request: ImageRequest, target: ImageView) {
        // cover previous request input same target
        pausedRequests.put(System.identityHashCode(target), request)
    }

    fun start(request: ImageRequest?,memoryCache: MemoryCache) {
        if (request == null) {
            return
        }

        var imageView: ImageView? = null
        if (request.targetReference != null) {
            imageView = request.targetReference!!.get()
            if (imageView != null) {
                if (checkTag(
                        request,
                        imageView
                    )
                ) {
                    return
                }
                if (!request.keepOriginal) {
                    imageView.setImageDrawable(null)
                }
                imageView.tag = null
            }
        }

        // id source is invalid, just callback and return
        when(request.data){
            is String->{
                if (TextUtils.isEmpty(request.data)) {
                    abort(request, imageView)
                    return
                }
            }
        }

        var bitmap = memoryCache.get(request.key)

        val waiter = request.waiter
        if (waiter != null && (bitmap != null || waiter.timeout == 0L)) {
            waiter.result = bitmap
            return
        }

        feedback(
            request,
            imageView,
            bitmap,
            true
        )

        if (imageView != null && bitmap == null && pauseFlag) {
            pause(request, imageView)
            return
        }

        if (bitmap == null) {
            Coroutine.async {
                val filePath = DiskCache[request.key]
                val fromDiskCache = !TextUtils.isEmpty(filePath)
                val source = if (fromDiskCache) Source.valueOf(File(filePath!!)) else Source.parse(request)
                val gifDecoder = Config.gifDecoder
//                if (!fromDiskCache && request.gifPriority && gifDecoder != null
//                    && HeaderParser.isGif(source.magic)) {
//                    return gifDecoder.decode(source.data)
//                }
                bitmap = Decoder.decode(source, request, fromDiskCache)
                bitmap = transform(request, bitmap)
                if (bitmap != null) {
                    if (request.memoryCacheStrategy != MemoryCacheStrategy.NONE) {
                        val toWeakCache = request.memoryCacheStrategy == MemoryCacheStrategy.WEAK
                        memoryCache.set(request.key, bitmap!!, toWeakCache)
                    }
                    if (!fromDiskCache && request.diskCacheStrategy and DiskCacheStrategy.RESULT != 0) {
                        DiskCache.put(request.key, bitmap!!)
                    }
                }
            }.onSuccess {  }
//            val loader = Worker(request, imageView)
//            loader.priority(request.priority)
//                    .hostHash(request.hostHash)
//                    .execute()
//
//            if (waiter != null && !loader.isDone && !loader.isCancelled) {
//                try {
//                    @Suppress("ReplaceGetOrSet")
//                    waiter.result = loader.get(waiter.timeout, TimeUnit.MILLISECONDS) as Bitmap
//                } catch (t: Throwable) {
//                    LogProxy.e(TAG, t)
//                }
//
//                if (!loader.isDone) {
//                    loader.cancel(true)
//                }
//            }
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
    private fun checkTag(request: ImageRequest, imageView: ImageView): Boolean {
//        val tag = imageView.tag
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
//            val e = IllegalArgumentException("Don't call setTag() on a view Doodle is targeting, try setTag(int, Object)")
//            return true
//            // shell we throw the exception ?
//            // throw e;
//        }
        return false
    }

    private fun abort(request: ImageRequest, imageView: ImageView?) {
//        if (request.simpleTarget != null) {
//            request.simpleTarget!!.onComplete(null)
//        } else if (imageView != null) {
//            if (request.callback != null && request.callback!!.onReady(null)) {
//                return
//            }
//            if (request.goneIfMiss || request.errorResId >= 0 || request.errorDrawable != null) {
//                setError(
//                    request,
//                    imageView
//                )
//            } else {
//                setPlaceholder(
//                    request,
//                    imageView
//                )
//            }
//        }
    }

    fun feedback(request: ImageRequest, imageView: ImageView?, result: Any?, beforeLoading: Boolean) {
        //  no matter cancel or not, try to stop animated drawable ( if set )
        if (!beforeLoading && request.targetReference != null) {
            stopAnimDrawable(request.targetReference!!.get())
        }

        if (request.waiter != null) {
            return
        }

        val bitmap = result as? Bitmap
        val isFinished = !beforeLoading || bitmap != null

//        if (isFinished && request.simpleTarget != null) {
//            request.simpleTarget!!.onComplete(bitmap)
//            return
//        }

        if (imageView == null) {
            return
        }

//        if (isFinished && request.callback != null) {
//            if (request.callback!!.onReady(bitmap)) {
//                return
//            }
//        }

        if (result != null) {
            if (bitmap != null) {
                if (request.alwaysAnimation || !beforeLoading) {
                    if (request.crossFade) {
                        crossFade(
                            imageView,
                            bitmap,
                            request
                        )
                    } else {
                        imageView.setImageBitmap(bitmap)
                        startAnimation(
                            request,
                            imageView
                        )
                    }
                } else {
                    imageView.setImageBitmap(bitmap)
                }
            } else if (result is Drawable) {
                imageView.setImageDrawable(result)
            }
        } else {
            if (beforeLoading) {
                setPlaceholder(
                    request,
                    imageView
                )
                val placeholder = imageView.drawable
                if (placeholder is Animatable && !pauseFlag) {
                    placeholder.start()
                }
            } else {
                setError(
                    request,
                    imageView
                )
            }
        }
    }

    private fun stopAnimDrawable(target: ImageView?) {
        if (target != null) {
            val placeholder = target.drawable
            if (placeholder is Animatable) {
                (placeholder as Animatable).stop()
            }
        }
    }

    private fun crossFade(imageView: ImageView, bitmap: Bitmap, request: ImageRequest) {
        var previous: Drawable? = imageView.drawable
        if (previous == null) {
            previous = ColorDrawable(Color.TRANSPARENT)
        }
        @Suppress("DEPRECATION")
        val current = BitmapDrawable(bitmap)
        val drawable = TransitionDrawable(arrayOf(previous, current))
        drawable.isCrossFadeEnabled = true
        imageView.setImageDrawable(drawable)
        drawable.startTransition(request.crossFadeDuration)
    }

    private fun startAnimation(request: ImageRequest, imageView: ImageView) {
        if (request.animation != null) {
            imageView.clearAnimation()
            imageView.startAnimation(request.animation)
        } else if (request.animationId != 0) {
            try {
                val animation = AnimationUtils.loadAnimation(context, request.animationId)
                imageView.clearAnimation()
                imageView.startAnimation(animation)
            } catch (e: Exception) {
            }
        }
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
}
