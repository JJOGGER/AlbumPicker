package com.example.album_helper.datasource

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import com.example.album_helper.callback.OnDataLoadedListener
import com.example.album_helper.callback.OnDataSupportListener
import com.example.album_helper.coroutine.Coroutine
import com.example.album_helper.helper.AlbumHelper
import com.example.album_helper.model.ImageFolder
import com.example.album_helper.model.ImageItem
import com.example.album_helper.util.SupportType
import kotlinx.coroutines.CoroutineScope
import java.io.File

class ImageDataSource(
    activity: FragmentActivity,
    private val scope: CoroutineScope,
    path: String?,
    albumHelper: AlbumHelper,
    loadedListener: OnDataLoadedListener
) : LoaderManager.LoaderCallbacks<Cursor> {
    private var loadFinished: Boolean = false
    private val path = path
    private val QUERY_URI = MediaStore.Files.getContentUri("external")
    private val activity = activity
    private lateinit var imageFolders: ArrayList<ImageFolder>
    private val filterDir: String? = null
    private val albumHelper = albumHelper

    private var isBatch = false

    private val isExcute = false

    private val loadedListener: OnDataLoadedListener? = loadedListener
    private val mDataSupportListener: OnDataSupportListener? = null
    private var imageSupportFormat: IntArray? = null
    private val IMAGE_PROJECTION by lazy {
        arrayOf("_display_name", "_data", "_size", "width", "height", "mime_type", "date_added")
    }

    // =============================================
    private val SELECTION_FOR_SINGLE_MEDIA_TYPE =
        (MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
                + " AND " + MediaStore.MediaColumns.SIZE + ">0"
                + ") GROUP BY (bucket_id")
    private val PROJECTION = arrayOf(
        MediaStore.Files.FileColumns._ID,
        "bucket_id",
        "bucket_display_name",
        MediaStore.MediaColumns.DATA,
        "COUNT(*) AS " + "count"
    )
    private val SELECTION = ("(" + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?"
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "=?)"
            + " AND " + MediaStore.MediaColumns.SIZE + ">0"
            + ") GROUP BY (bucket_id")
    private val SELECTION_ARGS = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
    )

    init {
        imageFolders = arrayListOf()
        imageSupportFormat = with(albumHelper.getImageSupportFormat()) {
            if (this != null) {
                this
            } else {
                SupportType.getSupportImageType()
            }
        }
//        filterDir=albumHelper.
    }

    fun initLoader() {
        if (path == null) {
            LoaderManager.getInstance(activity).initLoader(0, null, this)
        } else {
            val bundle = Bundle()
            bundle.putString("path", path)
            LoaderManager.getInstance(activity).initLoader(1, null, this)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        loadFinished = false
        var selection: String? = null
        when (id) {
            1 -> {
                selection =
                    IMAGE_PROJECTION[1] + " like '%" + args!!.getString("path") + "%'"
            }
        }
        return CursorLoader(
            this.activity,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            IMAGE_PROJECTION,
            selection,
            null,
            IMAGE_PROJECTION[6] + " DESC"
        )


    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        Coroutine.async(scope) {
            try {
                fun dataBatchHandler(i: Int) {
                    if (i > 0 && i % 1000 == 0) {
                        albumHelper.setImageFolders(imageFolders)
                        loadedListener!!.onDataLoaded(imageFolders, false)
                    }
                }

                fun buildImageFolder(
                    data: Cursor,
                    allImages: ArrayList<ImageItem>?
                ) {
                    if (data.count > 0) {
                        val allImagesFolder = ImageFolder("全部图片", "/")
                        if (allImages != null && allImages.size > 0) {
                            allImagesFolder.cover = allImages[0]
                        }
                        allImagesFolder.images = allImages
                        imageFolders.add(0, allImagesFolder)
                    }
                }

                var i = 0
                imageFolders.clear()
                val allImages: ArrayList<ImageItem> = arrayListOf()
                if (data != null) {
                    val count = data.count
                    while (data.moveToNext()) {
                        val imageName = data.getString(
                            data.getColumnIndexOrThrow(
                                IMAGE_PROJECTION[0]
                            )
                        )
                        val imagePath = data.getString(
                            data.getColumnIndexOrThrow(
                                IMAGE_PROJECTION[1]
                            )
                        )
                        val imageSize = data.getLong(
                            data.getColumnIndexOrThrow(
                                IMAGE_PROJECTION[2]
                            )
                        )
                        val imageWidth = data.getInt(
                            data.getColumnIndexOrThrow(
                                IMAGE_PROJECTION[3]
                            )
                        )
                        val imageHeight = data.getInt(
                            data.getColumnIndexOrThrow(
                                IMAGE_PROJECTION[4]
                            )
                        )
                        val imageMimeType = data.getString(
                            data.getColumnIndexOrThrow(
                                IMAGE_PROJECTION[5]
                            )
                        )
                        val imageAddTime = data.getLong(
                            data.getColumnIndexOrThrow(
                                IMAGE_PROJECTION[6]
                            )
                        )
                        val imageItem = ImageItem(
                            imageName,
                            imagePath,
                            imageSize,
                            imageWidth,
                            imageHeight,
                            imageMimeType,
                            imageAddTime
                        )
//                    imageItem.name = imageName
//                    imageItem.path = imagePath
//                    imageItem.size = imageSize
//                    imageItem.width = imageWidth
//                    imageItem.height = imageHeight
//                    imageItem.mimeType = imageMimeType
//                    imageItem.addTime = imageAddTime
//                    imageItem.createTime = imageAddTime * 1000L
//                    DateUtil.setTimeFormat(imageItem)
//                    if (ImagePickerInner.getInstance().getSelectImageCount() > 0) {
//                        imageItem.canVideoSelect = false
//                    } else {
//                        imageItem.canVideoSelect = true
//                    }
                        var fileExists=if (!TextUtils.isEmpty(imagePath)) File(
                            imageItem.path!!
                        ).exists() else false
                        if (TextUtils.isEmpty(imageItem.path) || TextUtils.isEmpty(filterDir) || !imageItem.path!!.contains(
                                filterDir!!
                            )
                        ) {
                            if (imageItem.size > 0L && fileExists
                            ) {
                                var support: Boolean
                                if (!TextUtils.isEmpty(imageMimeType)) {
                                    support = !SupportType.compareMimeType(
                                        imageSupportFormat,
                                        imageMimeType
                                    )
                                    if (support) {
                                        Log.e(
                                            "ImageDataSource",
                                            "不支持的图片类型：$imageMimeType; 图片：$imagePath"
                                        )
                                        continue
                                    }
                                }
                                if (mDataSupportListener != null) {
//                                support = mDataSupportListener.isImageSupport(
//                                    imagePath,
//                                    imageWidth,
//                                    imageHeight,
//                                    imageSize,
//                                    imageMimeType
//                                )
                                    support =
                                        mDataSupportListener.isImageSupport()
                                    if (!support) {
                                        Log.e(
                                            "ImageDataSource",
                                            "不支持的图片：width:$imageWidth;height:$imageHeight;size:$imageSize;mimeType:$imageMimeType; 路径：$imagePath"
                                        )
                                        continue
                                    }
                                }
                                allImages.add(imageItem)
                            }
                            if (!TextUtils.isEmpty(imagePath)) {
                                val imageFile = File(imagePath)
                                if (imageFile.exists()) {
                                    val imageParentFile = imageFile.parentFile
                                    val imageFolder = ImageFolder(
                                        name = imageParentFile.name,
                                        path = imageParentFile.absolutePath
                                    )
                                    if (!imageFolders.contains(imageFolder)) {
                                        val images: ArrayList<ImageItem> =
                                            arrayListOf()
                                        if (imageItem.size > 0L && fileExists) {
                                            images.add(imageItem)
                                        }
                                        imageFolder.cover = imageItem
                                        imageFolder.images = images
                                      imageFolders.add(imageFolder)
                                    } else if (imageItem.size > 0L && !TextUtils.isEmpty(imageItem.path) && File(
                                            imageItem.path!!
                                        ).exists()
                                    ) {
                                        (imageFolders.get(
                                            imageFolders.indexOf(
                                                imageFolder
                                            )
                                        )).images!!.add(imageItem)
                                    }
                                }
                            }
                            if (count >= 5000) {
                                ++i
                               isBatch = true
                                dataBatchHandler(i)
                            }
                        }
                    }
                }
                if (!isBatch) {
                    buildImageFolder(data!!, allImages)
                    albumHelper.setImageFolders(imageFolders)
                    loadedListener!!.onDataLoaded(
                        imageFolders,
                        true
                    )
                } else if (!isExcute) {
                    isBatch = !isBatch
                    buildImageFolder(data!!, allImages)
                    albumHelper.setImageFolders(imageFolders)
                    loadedListener!!.onDataLoaded(
                        imageFolders,
                        true
                    )
                }
            } catch (var18: IllegalArgumentException) {
                var18.printStackTrace()
            } catch (var19: Exception) {
                var19.printStackTrace()
            }

        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        TODO("Not yet implemented")
    }

}