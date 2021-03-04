package com.example.imageload.cache

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.net.Uri

internal class ImageContentProvider:ContentProvider(){
    companion object {
        @SuppressLint("StaticFieldLeak")
        internal lateinit var ctx: Context
            private set
    }

    override fun onCreate(): Boolean {
        ctx = checkNotNull(context)
        return true
    }

    override fun insert(uri: Uri,
                        values: ContentValues?) = null

    override fun query(uri: Uri,
                       projection: Array<String>?,
                       selection: String?,
                       selectionArgs: Array<String>?,
                       sortOrder: String?) = null

    override fun update(uri: Uri,
                        values: ContentValues?,
                        selection: String?,
                        selectionArgs: Array<String>?) = 0

    override fun delete(uri: Uri,
                        selection: String?,
                        selectionArgs: Array<String>?) = 0

    override fun getType(uri: Uri) = null
}