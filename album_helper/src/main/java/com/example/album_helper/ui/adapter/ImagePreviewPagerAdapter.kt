package com.example.album_helper.ui.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.example.album_helper.model.ImageItem
import com.example.imageload.cache.constant.DiskCacheStrategy
import com.github.chrisbanes.photoview.PhotoView
import load

internal class ImagePreviewPagerAdapter(val activity: Activity,val imageItems: ArrayList<ImageItem>) : PagerAdapter() {

    override fun isViewFromObject(view: View, `object`: Any) = view == `object`

    override fun getCount() = imageItems.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val photoView = PhotoView(container.context)
        photoView.load(imageItems[position].path,
            builder = { diskCacheStrategy(DiskCacheStrategy.NONE) })
        photoView.setOnClickListener {
            callBack?.onPhotoViewClick()
        }
        container.addView(photoView)
        return photoView
    }

    override fun getItemPosition(`object`: Any): Int {
        return POSITION_NONE
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    private val callBack: CallBack?
        get() = activity as CallBack

    interface CallBack {
        fun onPhotoViewClick()
    }
}