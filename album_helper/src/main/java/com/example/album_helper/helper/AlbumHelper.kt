package com.example.album_helper.helper

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.album_helper.constant.ImageSelectMode
import com.example.album_helper.model.ImageFolder
import com.example.album_helper.model.ImageItem
import com.example.album_helper.permission.Permissions
import com.example.album_helper.permission.PermissionsCompat
import com.example.album_helper.ui.SelectImageActivity

class AlbumHelper(builder: Builder) {
    private var imagefolder: ImageFolder? = null
    private var imageItem: ImageItem? = null
    private val imageSelectMode: Int

    init {
        imageSelectMode = builder.imageSelectMode
    }

    companion object {
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

    fun start(ctx: AppCompatActivity) {
        PermissionsCompat.Builder(ctx)
            .addPermissions(*Permissions.Group.STORAGE)
            .rationale("存储权限")
            .onGranted {
                ctx.startActivity(Intent(ctx, SelectImageActivity::class.java))
            }
            .request()

    }

    fun getImageSelectMode(): Int {
        return imageSelectMode
    }

    /**
     * 记录当前文件夹
     */
    internal fun setImageFolder(imageFolder: ImageFolder?) {
        this.imagefolder = imageFolder
    }

    internal fun getImageFolder(): ImageFolder? {
        return this.imagefolder
    }

    /**
     * 记录当前文件
     */
    internal fun setImageItem(imageItem: ImageItem) {
        this.imageItem = imageItem
    }

    internal fun getImageItem(): ImageItem? {
        return this.imageItem
    }

    class Builder {
        var imageSelectMode: Int

        init {
            imageSelectMode = ImageSelectMode.MODE_DEFAULT
        }

        fun setImageSelectMode(imageSelectMode: Int) = apply {
            this.imageSelectMode = imageSelectMode
        }

        fun build(): AlbumHelper {
            return AlbumHelper(this)
        }
    }
}

