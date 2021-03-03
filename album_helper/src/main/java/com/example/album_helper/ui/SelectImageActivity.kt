package com.example.album_helper.ui

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.album_helper.R
import com.example.album_helper.helper.AlbumHelper
import com.example.album_helper.model.ImageFolder
import com.example.album_helper.ui.adapter.ImageSelectAdapter
import com.example.album_helper.ui.viewmodel.SelectImageViewModel
import com.example.album_helper.util.getViewModel
import kotlinx.android.synthetic.main.activity_select_image.*
import java.util.ArrayList

class SelectImageActivity : BaseImageActivity<SelectImageViewModel>() {
    private val TAG=SelectImageActivity::class.java.simpleName
    private val albumHelper = AlbumHelper.get()
    private val adapter: ImageSelectAdapter by lazy {
        ImageSelectAdapter(this).apply {

        }
    }
    override val viewModel: SelectImageViewModel
        get() = getViewModel(SelectImageViewModel::class.java)

    override fun getLayoutId(): Int = R.layout.activity_select_image

    override fun initView(savedInstanceState: Bundle?) {
        rv_content.layoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, true)
        rv_content.adapter = adapter
        viewModel.getImageFoldersLiveData().observe(this, Observer { updateView(it) })
        viewModel.loadImageDatas(this, albumHelper)
    }

    private fun updateView(it: ArrayList<ImageFolder>?) {
        if (it != null && it.size != 0) {
            val imageFolder = it.get(0)
            adapter.setItems(imageFolder.images)
        } else {
            adapter.setItems(null)
        }

    }

}