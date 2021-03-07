package com.example.imageload.loader

import android.content.Context

object LoaderManager {
    private var imageLoader: ImageLoader? = null

    fun getImageLoader(context: Context): ImageLoader = imageLoader ?: newImageLoader(context)

    @Synchronized
    private fun newImageLoader(context: Context): ImageLoader {
        imageLoader?.let { return it }

        val newImageLoader = ImageLoader(context)
        imageLoader = newImageLoader
        return newImageLoader
    }
}