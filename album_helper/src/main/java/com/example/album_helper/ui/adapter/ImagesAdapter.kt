package com.example.album_helper.ui.adapter

import android.content.Context
import com.example.album_helper.R
import com.example.album_helper.model.ImageItem
import com.example.album_helper.util.ScreenUtil
import com.example.imageload.cache.constant.DiskCacheStrategy
import kotlinx.android.synthetic.main.rv_images_item.view.*
import load

class ImagesAdapter(context: Context) :
    SimpleRecyclerAdapter<ImageItem>(context, R.layout.rv_images_item) {
    private val imageSize = ScreenUtil.deviceWidth(context) / 4
    override fun convert(holder: ItemViewHolder, item: ImageItem, payloads: MutableList<Any>) {
        with(holder.itemView) {
            iv_image.let {
                iv_image.layoutParams.width = imageSize
                iv_image.layoutParams.height = imageSize
                iv_image.load(
                    item.path,
                    builder = {
                        diskCacheStrategy(DiskCacheStrategy.NONE)
                        override(ScreenUtil.dp2Px(100), ScreenUtil.dp2Px(100))
                    })
            }
        }
    }

    override fun registerListener(holder: ItemViewHolder) {
    }
}