package com.example.album_helper.ui.adapter

import android.content.Context
import com.example.album_helper.R
import com.example.album_helper.model.ImageItem
import com.example.album_helper.util.deviceWidth
//import com.horizon.doodle.Doodle
import kotlinx.android.synthetic.main.rv_image_select_item.view.*
import load

class ImageSelectAdapter(context:Context): SimpleRecyclerAdapter<ImageItem>(context, R.layout.rv_image_select_item) {
    private val imageSize = deviceWidth(context) / 4
    override fun convert(holder: ItemViewHolder, item: ImageItem, payloads: MutableList<Any>) {
        with(holder.itemView){
//            Glide.with(context)
//                .load(item.path)
//                .into(iv_image)
//            iv_image.loadAny(item.path)
//            Doodle.load(item.path!!)
//                .into(iv_image)
            iv_image.load(item.path)
//            iv_image.setImageBitmap(BitmapFactory.decodeFile(item.path))
        }
    }

    override fun registerListener(holder: ItemViewHolder) {
        holder.itemView.setOnClickListener {

        }
    }
}