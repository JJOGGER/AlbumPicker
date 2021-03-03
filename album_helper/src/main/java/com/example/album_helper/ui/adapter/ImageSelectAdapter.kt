package com.example.album_helper.ui.adapter

import android.content.Context
import com.example.album_helper.R
import com.example.album_helper.model.ImageItem

class ImageSelectAdapter(context:Context): SimpleRecyclerAdapter<ImageItem>(context, R.layout.rv_image_select_item) {
    override fun convert(holder: ItemViewHolder, item: ImageItem, payloads: MutableList<Any>) {

    }

    override fun registerListener(holder: ItemViewHolder) {
        holder.itemView.setOnClickListener {

        }
    }
}