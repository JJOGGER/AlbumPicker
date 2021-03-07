package com.example.album_helper.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.album_helper.R
import com.example.album_helper.model.ImageFolder
import com.example.album_helper.ui.adapter.FoldersAdapter

class AlbumsSpinner(context: Context, val callback: AlbumsSpinnerCallBack) {
    private val adapter: FoldersAdapter by lazy { FoldersAdapter(context) }
    private var popupWindow: PopupWindow

    init {
        val contentView =
            LayoutInflater.from(context).inflate(R.layout.album_folder_window_layout, null)
        val rvContent = contentView.findViewById<RecyclerView>(R.id.rv_content)
        rvContent.layoutManager = LinearLayoutManager(context)
        rvContent.adapter = adapter
        popupWindow = PopupWindow(
            contentView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        adapter.setOnItemClickListener { holder, item ->
            callback.albumFolderSelected(item)
            popupWindow.dismiss()
        }
        with(popupWindow) {
            this.isFocusable = false
            this.isOutsideTouchable = false
        }
    }

    fun showWindow(dropDownView: View) {
        popupWindow.showAsDropDown(dropDownView)
    }

    fun dismissWindow() {
        popupWindow.dismiss()
    }

    fun updateDatas(folders: ArrayList<ImageFolder>?) {
        adapter.setItems(folders)
        adapter.notifyDataSetChanged()
    }

    interface AlbumsSpinnerCallBack {
        fun albumFolderSelected(item: ImageFolder)
    }

}