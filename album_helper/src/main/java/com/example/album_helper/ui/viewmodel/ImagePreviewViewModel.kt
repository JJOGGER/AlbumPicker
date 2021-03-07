package com.example.album_helper.ui.viewmodel

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.album_helper.helper.AlbumHelper
import java.io.File

class ImagePreviewViewModel(application: Application) : BaseViewModel(application) {
    private val albumHelper: AlbumHelper

    init {
        albumHelper = AlbumHelper.get()
    }

    val deleteImageLiveData: MutableLiveData<Any> by lazy {
        MutableLiveData<Any>()
    }
    val deleteFolderLiveData: MutableLiveData<Any> by lazy {
        MutableLiveData<Any>()
    }

    fun deleteImage() {
        val imageItem = albumHelper.getImageItem() ?: return
        execute {
            Log.e("IIIIIIII", "-------delete")
            if (TextUtils.isEmpty(imageItem.path)) return@execute
            val file = File(imageItem.path!!)
            if (!file.exists()) return@execute
            if (file.delete()) {
                val imageFolder = AlbumHelper.get().getImageFolder()
                imageFolder?.images?.remove(imageItem)
                if (!imageFolder?.images.isNullOrEmpty()) {
                    deleteImageLiveData.postValue(1)
                    return@execute
                }
                val fileFolder = File(imageItem.path)
                fileFolder.delete()
                albumHelper.setImageFolder(null)
                deleteFolderLiveData.postValue(1)
            }
        }
    }
}