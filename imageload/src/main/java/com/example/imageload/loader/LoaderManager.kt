package com.example.imageload.loader

import android.content.Context

object LoaderManager {
    private var imageLoader:ImageLoader?=null

    fun getImageLoader(context: Context):ImageLoader= imageLoader?:ImageLoader(context)
}