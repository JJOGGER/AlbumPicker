package com.example.album_helper.util

object SupportType{
    fun compareMimeType(supportType: IntArray?, mimeTypeStr: String?): Boolean {
        val mineType: Int = MediaFile.getFileTypeForMimeType(mimeTypeStr)
        if ( supportType!=null) {
            for (element in supportType) {
                if (mineType == element) {
                    return true
                }
            }
        }
        return false
    }

    fun getSupportImageType(): IntArray? {
        return intArrayOf(MediaFile.FILE_TYPE_JPEG, MediaFile.FILE_TYPE_GIF,
            MediaFile.FILE_TYPE_PNG , MediaFile.FILE_TYPE_BMP  , MediaFile.FILE_TYPE_HEIC )
    }
}