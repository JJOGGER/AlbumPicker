package com.example.album_helper.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.imageload.coroutine.Coroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(application: Application) : AndroidViewModel(application),
    CoroutineScope by MainScope() {
    fun <T> execute(
        scope: CoroutineScope=this,
        context: CoroutineContext = Dispatchers.IO,
        block: suspend CoroutineScope.() -> T
    ): Coroutine<T> {
        return Coroutine.async(scope, context) { block() }
    }

    override fun onCleared() {
        super.onCleared()
        cancel()
    }

//    open fun toast(message: Int) {
//        launch {
//            context.toast(message)
//        }
//    }
}