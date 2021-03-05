package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.album_helper.helper.AlbumHelper

class MainActivity : AppCompatActivity() {
    private lateinit var albumHelper: AlbumHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        albumHelper= AlbumHelper.Builder()
            .build()
    }

    fun openAlbum(view: View) {
        albumHelper.start(this)
    }
}
