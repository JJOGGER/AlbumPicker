package com.example.album_helper.model

data class ImageFolder(val name:String?,
val path:String?,
                       var cover :ImageItem?=null,
var images:ArrayList<ImageItem>?=null) {
}