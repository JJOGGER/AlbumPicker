package com.example.album_helper.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ComputableLiveData
import androidx.lifecycle.MutableLiveData
import com.example.album_helper.callback.OnDataLoadedListener
import com.example.album_helper.datasource.ImageDataSource
import com.example.album_helper.helper.AlbumHelper
import com.example.album_helper.model.ImageFolder

class SelectImageViewModel(application: Application):BaseViewModel(application){
private  val imageFoldersLiveData: MutableLiveData<ArrayList<ImageFolder>> =
    MutableLiveData<ArrayList<ImageFolder>>()

    fun loadImageDatas(activity: FragmentActivity,albumHelper: AlbumHelper){
ImageDataSource(activity,null,albumHelper,object :OnDataLoadedListener{
    override fun onDataLoaded(imageFolders: ArrayList<ImageFolder>, var2: Boolean) {
        Log.e("SelectImageViewModel","-------${imageFolders.size}")
        imageFoldersLiveData.postValue(imageFolders)
    }
})
    }
fun getImageFoldersLiveData():MutableLiveData<ArrayList<ImageFolder>>{
    return imageFoldersLiveData
}
}