package com.example.imageload.loader

import android.widget.ImageView
import com.example.imageload.cache.MemoryCache
import com.example.imageload.cache.MemoryCacheImpl
import com.example.imageload.request.ImageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

/**
 * loader实现
 */
class ImageLoaderImpl(override val memoryCache:MemoryCacheImpl):ImageLoader {

    private var targetReference:WeakReference<ImageView>?=null
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate )
    override fun load(request: ImageRequest) {
        if (request.target==null)
            return
        targetReference= WeakReference(request.target!!)
        when{
            request.target!!.width>0&&request.target!!.height>0->{

            }
        }

    }

    private fun loadImage(targetWidth:Int,targetHeight:Int){

        scope.launch {
            if (targetReference!=null) {
                val target= targetReference!!.get()

            }

        }
    }
}