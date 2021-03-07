package com.example.album_helper.ui.dialog

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.album_helper.R
import kotlinx.android.synthetic.main.dialog_delete_image.*

class DeleteImageDialog : BaseDialogFragment() {

    override fun onStart() {
        super.onStart()
        val dm = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(dm)
        dialog?.window?.setLayout(
            (dm.widthPixels * 0.85).toInt(),
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_delete_image, container)
    }

    override fun onFragmentCreated(view: View, savedInstanceState: Bundle?) {
        tv_delete.setOnClickListener {
            callBack?.comfirmDelete()
            dismiss()
        }
        tv_cancel.setOnClickListener {
            dismiss()
        }
    }

    private val callBack: CallBack?
        get() = (parentFragment as? CallBack) ?: (activity as? CallBack)

    interface CallBack {
        fun comfirmDelete()
    }
}