package com.example.imageload.loader

import android.content.Context
import com.example.imageload.cache.MemoryCache
import com.example.imageload.cache.MemoryCacheImpl
//import com.example.imageload.request.Disposable
import com.example.imageload.request.ImageRequest

interface ImageLoader {
    val memoryCache: MemoryCache
    fun load(request:ImageRequest)

    class Builder{
        private val context:Context
        private var memoryCache: MemoryCacheImpl?
        constructor(context: Context){
            this.context=context.applicationContext
            memoryCache=null

        }

        fun build():ImageLoader{
            val memoryCache =memoryCache?:buildDefaultMemoryCache()
            return ImageLoaderImpl(memoryCache=memoryCache)
        }

        private fun buildDefaultMemoryCache():MemoryCacheImpl{
            return MemoryCacheImpl()
        }
    }
    companion object {
        /** Create a new [ImageLoader] without configuration. */
        @JvmStatic
        @JvmName("create")
        operator fun invoke(context: Context) = Builder(context).build()
    }
}