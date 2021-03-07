package com.example.album_helper.ui.adapter

import android.content.Context
import com.example.album_helper.R
import com.example.album_helper.model.ImageFolder
import com.example.imageload.cache.constant.DiskCacheStrategy
import kotlinx.android.synthetic.main.rv_folders_item.view.*
import kotlinx.android.synthetic.main.rv_images_item.view.iv_image
import loadAny

class FoldersAdapter(context: Context) :
    SimpleRecyclerAdapter<ImageFolder>(context, R.layout.rv_folders_item) {
    override fun convert(holder: ItemViewHolder, item: ImageFolder, payloads: MutableList<Any>) {
        with(holder.itemView) {
            tv_folder_name.text = item.name
            tv_folder_num.text = "${item.images?.size}张"
            if (!item.images.isNullOrEmpty()) {
                iv_image.loadAny(
                    item.images!![0].path,
                    builder = { diskCacheStrategy(DiskCacheStrategy.NONE) })
            }
        }
    }

    override fun registerListener(holder: ItemViewHolder) {
    }

}