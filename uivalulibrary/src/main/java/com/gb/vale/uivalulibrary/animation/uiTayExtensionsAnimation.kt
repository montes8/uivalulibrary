package com.gb.vale.uivalulibrary.animation

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.TranslateAnimation
import com.gb.vale.uivalulibrary.utils.uiTayGone
import com.gb.vale.uivalulibrary.utils.uiTayHandler
import com.gb.vale.uivalulibrary.utils.uiTayVisible


/**Appearance animation from alpha 0 to its original color*/
fun View.fadeZeroToOne(duration:Long = 1300) {
    val fadeOut = AlphaAnimation(
        0f,
        1f
    )
    fadeOut.interpolator = AccelerateInterpolator()
    fadeOut.startOffset = 500
    fadeOut.duration = duration
    uiTayHandler(duration) {  this.uiTayVisible()}
    this.animation = fadeOut
}

/**Disappearance animation from original color to alphs 0*/
fun View.fadeOneToZero(duration:Long = 1300) {
    val fadeOut = AlphaAnimation(
        1f,
        0f
    )
    fadeOut.interpolator = AccelerateInterpolator()
    fadeOut.startOffset = 500
    fadeOut.duration = duration
    uiTayHandler(duration) {  this.uiTayGone()}

    this.animation = fadeOut
}

/**rebound successive effect animation*/
fun View.uiTayDoBounce(duration : Long = 250,distanceRebound : Float = -15.0f) {

    val mAnimation = TranslateAnimation(0f, 0f, 0f, distanceRebound)
    mAnimation.duration = duration
    mAnimation.repeatCount = -1
    mAnimation.repeatMode = Animation.REVERSE
    mAnimation.interpolator = LinearInterpolator()
    this.startAnimation(mAnimation)
}

/**rebound effect animation*/
fun View.uiTayDoBounceAnimation(duration : Long = 1000) {
    val animator = ObjectAnimator.ofFloat(
        this,
        "translationY",
        this.measuredHeight.toFloat(),
        -30f,
        10f,
        0f
    )
    val animator2 = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f, 1f, 1f)
    animator.duration = duration
    animator.start()
    animator2.duration = duration
    animator2.start()
}

/** The view disappears with an animation that contracts from top to bottom*/
fun View.uiTayScaleUpView(duration: Int = 500){
    val height = this.height
    uiTayExtendHeightView(height, 0, this,
        duration, object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                //not implementation
            }
            override fun onAnimationEnd(animator: Animator) { uiTayGone()}

            override fun onAnimationCancel(animator: Animator) {
                //not implementation
            }
            override fun onAnimationRepeat(animator: Animator) {
                //not implementation
            }
        }).start()

}

/**A distance value is assigned from which it will slide to its real position from bottom to top.*/
fun View.uiTaySlideUp(duration: Long = 500,heightInit : Int = 850) {

    this@uiTaySlideUp.uiTayVisible()
    val animate = TranslateAnimation(
        0f,
        0f,
        (this.height + heightInit).toFloat(),
        0f) // toYDelta
    animate.duration = duration
    animate.fillAfter = true
    this.startAnimation(animate)
}

/**assign a higher height value so that it is completely hidden downwards*/
fun View.uiTaySlideDown(duration: Long = 500,heightInit : Int = 850) {
    val animate = TranslateAnimation(
        0f,
        0f,
        0f,
        (
                this.height + heightInit).toFloat()
    )
    animate.duration = duration
    animate.fillAfter = true
    animate.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            //not implementation
        }
        override fun onAnimationEnd(animation: Animation) {
            this@uiTaySlideDown.uiTayGone()
        }
        override fun onAnimationRepeat(animation: Animation) {
            //not implementation
        }
    })

    this.startAnimation(animate)
}


fun uiTayExtendHeightView(
    start: Int,
    end: Int,
    view: View,
    duration: Int,
    animatorListener: Animator.AnimatorListener?
): ValueAnimator {
    val animator = ValueAnimator.ofInt(start, end)
    animator.addUpdateListener { valueAnimator ->
        val value = valueAnimator.animatedValue as Int
        val layoutParams = view.layoutParams
        layoutParams.height = value
        view.layoutParams = layoutParams
    }
    animator.duration = duration.toLong()
    if (animatorListener != null)
        animator.addListener(animatorListener)
    return animator
}