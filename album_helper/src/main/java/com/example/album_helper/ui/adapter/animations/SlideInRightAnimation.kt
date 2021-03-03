package com.example.album_helper.ui.adapter.animations

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.View
import com.example.album_helper.ui.adapter.animations.BaseAnimation


class SlideInRightAnimation : BaseAnimation {


    override fun getAnimators(view: View): Array<Animator> =
        arrayOf(ObjectAnimator.ofFloat(view, "translationX", view.rootView.width.toFloat(), 0f))
}
