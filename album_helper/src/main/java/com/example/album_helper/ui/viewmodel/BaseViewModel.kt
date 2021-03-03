package com.example.album_helper.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.album_helper.coroutine.Coroutine
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel(application: Application):AndroidViewModel(application),
CoroutineScope by MainScope(){
fun <T> execute(
 scope: CoroutineScope,
context: CoroutineContext=Dispatchers.IO,
block:suspend CoroutineScope.()->T
):Coroutine<T>{
    return Coroutine.async(scope, context){block()}
}

    override fun onCleared() {
        super.onCleared()
        cancel()
    }
}