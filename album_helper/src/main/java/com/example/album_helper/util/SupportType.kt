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
        return intArrayOf(31, 32, 33, 34, 37)
    }
}