package com.example.album_helper.model

data class ImageItem(
    val name: String?,
    val path: String?,
    var size: Long = 0,
    var width: Int = 0,
    var height: Int = 0,
    var imageMimeType: String?=null,
    var imageAddTime: Long = 0
) {
}