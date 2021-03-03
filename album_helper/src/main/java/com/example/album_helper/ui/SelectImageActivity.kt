package com.example.album_helper.ui

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.album_helper.R
import com.example.album_helper.helper.AlbumHelper
import com.example.album_helper.ui.adapter.ImageSelectAdapter
import com.example.album_helper.ui.viewmodel.SelectImageViewModel
import com.example.album_helper.util.getViewModel
import kotlinx.android.synthetic.main.activity_select_image.*

class SelectImageActivity:BaseImageActivity<SelectImageViewModel>(){
private val albumHelper=AlbumHelper.get()
    override val viewModel: SelectImageViewModel
        get() = getViewModel(SelectImageViewModel::class.java)

    override fun getLayoutId(): Int = R.layout.activity_select_image

    override fun initView(savedInstanceState: Bundle?) {
        rv_content.layoutManager=GridLayoutManager(this,3,RecyclerView.VERTICAL,true)
        rv_content.adapter=ImageSelectAdapter(this).apply {

        }
        viewModel.loadImageDatas(this,albumHelper)
    }

}