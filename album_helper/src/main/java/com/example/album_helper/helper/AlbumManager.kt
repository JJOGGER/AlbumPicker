package com.example.album_helper.helper

import android.content.Context
import android.content.Intent
import com.example.album_helper.ui.SelectImageActivity

object AlbumManager {
    private var selectMode=0
    private val maxLimit=10
    fun start(ctx: Context){
        ctx.startActivity(Intent(ctx, SelectImageActivity::class.java).apply {

        })
    }
}