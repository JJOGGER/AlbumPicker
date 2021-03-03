package com.example.album_helper.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BaseImageActivity<VM:ViewModel> : AppCompatActivity(),CoroutineScope by MainScope() {
protected abstract val viewModel:VM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView(savedInstanceState)
    }

    open fun initView(savedInstanceState: Bundle?) {

    }

    abstract fun getLayoutId():Int

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

}