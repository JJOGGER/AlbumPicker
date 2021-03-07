package com.example.album_helper.model

data class ImageFolder(
    val name: String?,
    val path: String?,
    var cover: ImageItem? = null,
    var images: ArrayList<ImageItem>? = null
) {
    override fun equals(other: Any?): Boolean {
        return try {
            val (name1, path1) = other as ImageFolder
            path.equals(path1, ignoreCase = true) && name.equals(
                name1,
                ignoreCase = true
            )
        } catch (var3: ClassCastException) {
            var3.printStackTrace()
            super.equals(other)
        }
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (path?.hashCode() ?: 0)
        result = 31 * result + (cover?.hashCode() ?: 0)
        result = 31 * result + (images?.hashCode() ?: 0)
        return result
    }
}