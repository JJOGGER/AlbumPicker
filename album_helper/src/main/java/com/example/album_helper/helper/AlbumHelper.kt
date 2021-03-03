package com.example.album_helper.helper

import android.content.Context
import android.content.Intent
import com.example.album_helper.constant.ImageSelectMode
import com.example.album_helper.model.ImageFolder
import com.example.album_helper.ui.SelectImageActivity

class AlbumHelper(builder: Builder) {
    private var imagefolders: List<ImageFolder>? = null
    private val imageSelectMode = builder.imageSelectMode
    private val sInstance: AlbumHelper = this

    private var imageSupportFormat: IntArray? = null

    companion object {
        private lateinit var context: Context

        //        fun with(activity:Activity):AlbumManager{
//            context=activity.applicationContext
//            return A
//        }
        private var sInstance: AlbumHelper? = null
            get() {
                if (field == null) {
                    field = Builder().build()
                }
                return field
            }

        fun get(): AlbumHelper {
            return sInstance!!
        }
    }

    fun start(ctx: Context) {
        ctx.startActivity(Intent(ctx, SelectImageActivity::class.java).apply {

        })
    }

    fun getImageSelectMode(): Int{
        return imageSelectMode
    }

    fun getImageSupportFormat(): IntArray? {
        return imageSupportFormat
    }

    fun setImageFolders(imageFolders: List<ImageFolder>) {
        this.imagefolders = imageFolders
    }

    class Builder {
        internal var imageSelectMode = ImageSelectMode.MODE_DEFAULT
        internal var imageSupportFormat: IntArray? = null
        internal var imageFolders: List<ImageFolder>? = null

        fun setImageSelectMode(imageSelectMode: Int) {
            this.imageSelectMode = imageSelectMode
        }

        fun setImageFolders(imageFolders: List<ImageFolder>) {
            this.imageFolders = imageFolders
        }

        fun build(): AlbumHelper {
            return AlbumHelper(this)
        }
    }
}

