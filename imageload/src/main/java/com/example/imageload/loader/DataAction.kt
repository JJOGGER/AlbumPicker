package com.example.imageload.loader

import android.text.TextUtils

object DataAction {
    fun buildDataString(data:Any?):String{
        when(data){
            is String->{

                if (!TextUtils.isEmpty(data)){
                    return if (data.startsWith("http") || data.contains("://")) data else "file://$data"
                }
            }
        }
        return "NullRequestData"
    }
}