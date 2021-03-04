package com.example.album_helper.permission

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.album_helper.permission.RequestSource

import java.lang.ref.WeakReference

internal class ActivitySource(activity: AppCompatActivity) : RequestSource {

    private val actRef: WeakReference<AppCompatActivity> = WeakReference(activity)

    override val context: Context?
        get() = actRef.get()

    override fun startActivity(intent: Intent) {
        actRef.get()?.startActivity(intent)
    }

}
