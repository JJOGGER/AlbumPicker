package com.example.imageload.loader

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.os.Process
import android.text.TextUtils
import android.util.Log
import android.widget.ImageView
import com.example.imageload.cache.DiskCache
import com.example.imageload.cache.DiskCache.long2Hex
import com.example.imageload.cache.DiskCacheStrategy
import com.example.imageload.cache.MemoryCache
import com.example.imageload.cache.MemoryCacheStrategy
import com.example.imageload.decode.Decoder
import com.example.imageload.decode.HeaderParser
import com.example.imageload.decode.Source
import com.example.imageload.request.ImageRequest
import com.example.imageload.util.closeQuietly
import com.horizon.task.UITask
import com.horizon.task.base.LogProxy
import com.horizon.task.executor.LaneExecutor
import com.horizon.task.executor.PipeExecutor
import com.horizon.task.executor.TaskExecutor
import java.io.File
import java.io.InterruptedIOException
import java.lang.ref.WeakReference

internal class Worker(private val request: ImageRequest, imageView: ImageView?,memoryCache: MemoryCache) : UITask<Void, Void, Any>() {
    private val key: Long = request.key
    private val memoryCache=memoryCache

    private var fromDiskCache = false

    override val executor: TaskExecutor
        get() = loadingExecutor

    private val target: ImageView?
        get() {
            if (request.targetReference != null) {
                val imageView = request.targetReference!!.get()
                if (imageView != null && imageView.tag === request) {
                    return imageView
                }
            }
            return null
        }

    init {
        if (imageView != null) {
            request.workerReference = WeakReference(this)
            imageView.tag = request
        }
    }

    override fun generateTag(): String {
        val path = DataAction.buildDataString(request.data)
        return if (path.startsWith("http")) {
            TAG + path
        } else {
            long2Hex(key)
        }
    }

    override fun doInBackground(vararg params: Void): Any? {
        var bitmap: Bitmap? = null
        var source: Source? = null
        try {
            // check if target missed
            if (request.targetReference != null && target == null) {
                return null
            }

            bitmap = memoryCache.get(key)
            if (bitmap == null) {
//                MemoryCache.checkMemory()

                val filePath = DiskCache[key]
                fromDiskCache = !TextUtils.isEmpty(filePath)
                source = if (fromDiskCache) Source.valueOf(File(filePath!!)) else Source.parse(request)

                val gifDecoder = Config.gifDecoder
                if (!fromDiskCache && request.gifPriority && gifDecoder != null
                        && HeaderParser.isGif(source.magic)) {
                    return gifDecoder.decode(source.data)
                }

                bitmap = Decoder.decode(source, request, fromDiskCache)
                bitmap = transform(request, bitmap)
                if (bitmap != null) {
                    if (request.memoryCacheStrategy != MemoryCacheStrategy.NONE) {
                        val toWeakCache = request.memoryCacheStrategy == MemoryCacheStrategy.WEAK
                        memoryCache.set(key, bitmap, toWeakCache)
                    }
                    if (!fromDiskCache && request.diskCacheStrategy and DiskCacheStrategy.RESULT != 0) {
                        storeResult(key, bitmap)
                    }
                }
            }
            return bitmap
        } catch (e: InterruptedIOException) {
            if (LogProxy.isDebug) {
                Log.d(TAG, "loading cancel")
            }
        } catch (e: InterruptedException) {
            if (LogProxy.isDebug) {
                Log.d(TAG, "loading cancel")
            }
        } catch (e: Throwable) {
            LogProxy.e(TAG, e)
        } finally {
            if (fromDiskCache && bitmap == null) {
                DiskCache.delete(request.key)
            }
            closeQuietly(source)
        }
        return null
    }

    override fun onCancelled() {
        val imageView = target
        if (imageView != null) {
            imageView.tag = null
        }
//        request.simpleTarget = null
//        request.callback = null
        Dispatcher.feedback(request, null, null, false)
    }

    override fun onPostExecute(result: Any?) {
        val imageView = target
        if (imageView != null) {
            imageView.tag = null
        }
        Dispatcher.feedback(request, imageView, result, false)
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

    companion object {
        private val cpuCount = Runtime.getRuntime().availableProcessors()
        private val windowSize = Math.max(4, Math.min(cpuCount + 1, 6))
        private val loadingExecutor = LaneExecutor(PipeExecutor(windowSize))
        private val storageExecutor = PipeExecutor(1)

        private fun storeResult(key: Long, bitmap: Bitmap) {
            storageExecutor.execute {
                try {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
                    DiskCache.put(key, bitmap)
                } catch (e: Exception) {
                    LogProxy.e("Worker", e)
                }
            }
        }
    }
}

