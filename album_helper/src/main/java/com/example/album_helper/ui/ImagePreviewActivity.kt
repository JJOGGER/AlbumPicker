package com.example.album_helper.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.example.album_helper.R
import com.example.album_helper.constant.Extras
import com.example.album_helper.helper.AlbumHelper
import com.example.album_helper.ui.adapter.ImagePreviewPagerAdapter
import com.example.album_helper.ui.dialog.DeleteImageDialog
import com.example.album_helper.ui.viewmodel.ImagePreviewViewModel
import com.example.album_helper.util.getViewModel
import kotlinx.android.synthetic.main.activity_image_preview.*
import kotlinx.android.synthetic.main.activity_image_preview.ibtn_back
import kotlinx.android.synthetic.main.activity_image_preview.toolbar
import kotlinx.android.synthetic.main.activity_select_image.tv_title

class ImagePreviewActivity :
    BaseImageActivity<ImagePreviewViewModel>(), DeleteImageDialog.CallBack,
    ImagePreviewPagerAdapter.CallBack {
    private lateinit var albumHelper: AlbumHelper
    private lateinit var imagePreviewPagerAdapter: ImagePreviewPagerAdapter
    override fun getLayoutId() = R.layout.activity_image_preview
    override val viewModel: ImagePreviewViewModel
        get() = getViewModel(ImagePreviewViewModel::class.java)

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        val pos = intent.getIntExtra(Extras.SELECTED_IMAGE_POS, 0)
        albumHelper = AlbumHelper.get()
        albumHelper.setImageItem(albumHelper.getImageFolder()?.images!![pos])
        tv_title.text = "${pos + 1}/${albumHelper.getImageFolder()?.images!!.size}"
        imagePreviewPagerAdapter =
            ImagePreviewPagerAdapter(this, albumHelper.getImageFolder()?.images!!)
        vp_content.adapter = imagePreviewPagerAdapter
        vp_content.currentItem = pos
        vp_content.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                albumHelper.setImageItem(albumHelper.getImageFolder()?.images!![position])
                tv_title.text = "${position + 1}/${albumHelper.getImageFolder()?.images!!.size}"
            }
        })
        ibtn_back.setOnClickListener { finish() }
        ibtn_delete.setOnClickListener {
            val deleteImageDialog = DeleteImageDialog()
            deleteImageDialog.show(supportFragmentManager, "DeleteImageDialog")
        }
        viewModel.deleteImageLiveData.observe(this, Observer {
            deleteFileSuccess()
        })
        viewModel.deleteFolderLiveData.observe(this, Observer {
            deleteFolderSuccess()
        })
    }


    private fun deleteFileSuccess() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(Extras.ACTION_UPDATE_IMAGE))
        imagePreviewPagerAdapter.notifyDataSetChanged()
        tv_title.text =
            "${vp_content.currentItem + 1}/${albumHelper.getImageFolder()?.images!!.size}"
    }

    private fun deleteFolderSuccess() {
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(Extras.ACTION_UPDATE_FOLDER))
        finish()
    }

    override fun comfirmDelete() {
        viewModel.deleteImage()
    }

    override fun onPhotoViewClick() {
        toolbar.visibility =
            if (toolbar.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        bottom.visibility =
            if (bottom.visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
    }

}
