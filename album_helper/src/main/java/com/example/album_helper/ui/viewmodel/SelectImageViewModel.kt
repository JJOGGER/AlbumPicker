package com.example.album_helper.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.example.album_helper.callback.OnDataLoadedListener
import com.example.album_helper.datasource.ImageDataSource
import com.example.album_helper.helper.AlbumHelper
import com.example.album_helper.model.ImageFolder
import com.example.album_helper.util.isSDAvailable

class SelectImageViewModel(application: Application) : BaseViewModel(application) {
    private val TAG = SelectImageViewModel::class.java.simpleName
    val imageFoldersLiveData: MutableLiveData<ArrayList<ImageFolder>> by lazy { MutableLiveData<ArrayList<ImageFolder>>() }

    fun loadImageDatas(activity: FragmentActivity, albumHelper: AlbumHelper) {
        if (isSDAvailable()) {
            ImageDataSource(
                activity = activity,
                scope = this,
                albumHelper = albumHelper,
                loadedListener = object : OnDataLoadedListener {
                    override fun onDataLoaded(
                        imageFolders: ArrayList<ImageFolder>,
                        isLoadComplete: Boolean
                    ) {
                        Log.e("SelectImageViewModel", "-------${imageFolders.size}")
                        imageFoldersLiveData.postValue(imageFolders)
                    }
                }).initLoader()
        }
    }

}