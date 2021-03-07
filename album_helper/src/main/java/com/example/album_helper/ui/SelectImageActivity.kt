package com.example.album_helper.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.album_helper.R
import com.example.album_helper.constant.Extras
import com.example.album_helper.helper.AlbumHelper
import com.example.album_helper.model.ImageFolder
import com.example.album_helper.ui.adapter.ImagesAdapter
import com.example.album_helper.ui.viewmodel.SelectImageViewModel
import com.example.album_helper.util.getViewModel
import kotlinx.android.synthetic.main.activity_select_image.*
import java.lang.ref.WeakReference
import java.util.*

class SelectImageActivity : BaseImageActivity<SelectImageViewModel>(),
    AlbumsSpinner.AlbumsSpinnerCallBack {
    companion object {
        private val TAG = SelectImageActivity::class.java.simpleName
    }

    private lateinit var updateBroadcastReceiver: UpdateBroadcastReceiver
    private val albumHelper = AlbumHelper.get()
    private lateinit var albumsSpinner: AlbumsSpinner
    private val imagesAdapter: ImagesAdapter by lazy {
        ImagesAdapter(this).apply {
            setOnItemClickListener { holder, item ->
                startActivity(
                    Intent(
                        this@SelectImageActivity,
                        ImagePreviewActivity::class.java
                    ).apply {
                        val items = getItems()
                        var pos = 0
                        for (i in items) {
                            if (item == i) {
                                break
                            }
                            pos++
                        }
                        putExtra(Extras.SELECTED_IMAGE_POS, pos)
                    }
                )
            }
        }
    }
    override val viewModel: SelectImageViewModel
        get() = getViewModel(SelectImageViewModel::class.java)

    override fun getLayoutId(): Int = R.layout.activity_select_image

    override fun initView(savedInstanceState: Bundle?) {
        rv_content.layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        rv_content.adapter = imagesAdapter
        albumsSpinner = AlbumsSpinner(this, this)
        viewModel.imageFoldersLiveData.observe(this, Observer { updateView(it) })
        viewModel.loadImageDatas(this, albumHelper)
        tv_title.setOnClickListener {
            if (it.tag == null) {
                it.tag = true
            } else {
                it.tag = !(it.tag as Boolean)
            }
            showAlbumPopupWindow(it.tag as Boolean)
        }
        IntentFilter().apply {
            addAction(Extras.ACTION_UPDATE_FOLDER)
            addAction(Extras.ACTION_UPDATE_IMAGE)
        }
        updateBroadcastReceiver = UpdateBroadcastReceiver(this)
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(updateBroadcastReceiver, IntentFilter().apply {
                addAction(Extras.ACTION_UPDATE_FOLDER)
                addAction(Extras.ACTION_UPDATE_IMAGE)
            })
    }

    private fun showAlbumPopupWindow(isShow: Boolean) {
        if (isShow) {
            albumsSpinner.showWindow(toolbar)
        } else {
            albumsSpinner.dismissWindow()
        }

    }

    private fun updateView(it: ArrayList<ImageFolder>?) {
        if (it != null && it.size != 0) {
            val imageFolder = albumHelper.getImageFolder() ?: it.get(0)
            tv_title.text = imageFolder.name
            imagesAdapter.setItems(imageFolder.images)

        } else {
            imagesAdapter.setItems(null)
        }
        albumsSpinner.updateDatas(it)
    }

    override fun albumFolderSelected(item: ImageFolder) {
        tv_title.text = item.name
        imagesAdapter.setItems(item.images)
        albumHelper.setImageFolder(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateBroadcastReceiver)
    }

    inner class UpdateBroadcastReceiver(activity: SelectImageActivity) : BroadcastReceiver() {
        private var weakReference: WeakReference<SelectImageActivity> =
            WeakReference<SelectImageActivity>(activity)

        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1 == null) return
            val activity = weakReference.get()
            if (activity == null) return
            when (p1.action) {
                Extras.ACTION_UPDATE_FOLDER -> {
                    viewModel.loadImageDatas(activity, albumHelper)
                }
                Extras.ACTION_UPDATE_IMAGE -> {
                    imagesAdapter.setItems(albumHelper.getImageFolder()?.images)
                }
            }
        }

    }
}