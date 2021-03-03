package io.legado.app.base.adapter

import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.example.album_helper.ui.adapter.animations.*

/**
 * Created by Invincible on 2017/12/15.
 */
class ItemAnimation private constructor() {

    var itemAnimEnabled = false
    var itemAnimFirstOnly = true
    var itemAnimation: BaseAnimation? = null
    var itemAnimInterpolator: Interpolator = LinearInterpolator()
    var itemAnimDuration: Long = 300L
    var itemAnimStartPosition: Int = -1

    fun interpolator(interpolator: Interpolator): ItemAnimation {
        itemAnimInterpolator = interpolator
        return this
    }

    fun duration(duration: Long): ItemAnimation {
        itemAnimDuration = duration
        return this
    }

    fun startPostion(startPos: Int): ItemAnimation {
        itemAnimStartPosition = startPos
        return this
    }

    fun animation(animationType: Int = NONE, animation: BaseAnimation? = null): ItemAnimation {
        if (animation != null) {
            itemAnimation = animation
        } else {
            when (animationType) {
                FADE_IN -> itemAnimation = AlphaInAnimation()
                SCALE_IN -> itemAnimation = ScaleInAnimation()
                BOTTOM_SLIDE_IN -> itemAnimation = SlideInBottomAnimation()
                LEFT_SLIDE_IN -> itemAnimation = SlideInLeftAnimation()
                RIGHT_SLIDE_IN -> itemAnimation = SlideInRightAnimation()
            }
        }
        return this
    }

    fun enabled(enabled: Boolean): ItemAnimation {
        itemAnimEnabled = enabled
        return this
    }

    fun firstOnly(firstOnly: Boolean): ItemAnimation {
        itemAnimFirstOnly = firstOnly
        return this
    }

    companion object {
        const val NONE: Int = 0x00000000
        /**
         * Use with [.openLoadAnimation]
         */
        const val FADE_IN: Int = 0x00000001
        /**
         * Use with [.openLoadAnimation]
         */
        const val SCALE_IN: Int = 0x00000002
        /**
         * Use with [.openLoadAnimation]
         */
        const val BOTTOM_SLIDE_IN: Int = 0x00000003
        /**
         * Use with [.openLoadAnimation]
         */
        const val LEFT_SLIDE_IN: Int = 0x00000004
        /**
         * Use with [.openLoadAnimation]
         */
        const val RIGHT_SLIDE_IN: Int = 0x00000005

        fun create(): ItemAnimation {
            return ItemAnimation()
        }
    }
}