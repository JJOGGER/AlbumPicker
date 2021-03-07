package com.example.album_helper.callback

import com.example.album_helper.model.ImageFolder

interface OnDataLoadedListener {
    fun onDataLoaded(imageFolders:ArrayList<ImageFolder>,isLoadComplete:Boolean)
}