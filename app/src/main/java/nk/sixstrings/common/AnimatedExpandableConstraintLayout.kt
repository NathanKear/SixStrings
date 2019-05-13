package nk.sixstrings.common

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.constraintlayout.widget.ConstraintLayout


class AnimatedExpandableConstraintLayout(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private fun getDesiredExpandedHeight(): Int {
        this.measure(
                MeasureSpec.makeMeasureSpec(this.width, MeasureSpec.EXACTLY) /* Irrelevant, only care about measuring height */,
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        return this.measuredHeight
    }

    fun expand() {
        val targetHeight = getDesiredExpandedHeight()

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        this.layoutParams.height = 1
        this.visibility = View.VISIBLE
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                this@AnimatedExpandableConstraintLayout.layoutParams.height = if (interpolatedTime == 1f)
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                else
                    Math.max((targetHeight * interpolatedTime).toInt(), 1)
                this@AnimatedExpandableConstraintLayout.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms
        a.duration = 300
        this.startAnimation(a)
    }

    fun collapse() {
        val initialHeight = this.measuredHeight

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    this@AnimatedExpandableConstraintLayout.visibility = View.INVISIBLE
                } else {
                    this@AnimatedExpandableConstraintLayout.layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    this@AnimatedExpandableConstraintLayout.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms
        a.duration = 300
        this.startAnimation(a)
    }

//    fun expand(animationDurationMillis: Long) {
//
//        this.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        val height = this.measuredHeight.toFloat()
////        val height = this.height.toFloat()
////        this.layoutParams.width = WRAP_CONTENT
////        MeasureSpec.makeMeasureSpec()
//
//        ValueAnimator.ofFloat(0f, height).let {
//            this.visibility = View.VISIBLE
//
//            it.duration = animationDurationMillis
//            it.interpolator = AccelerateInterpolator()
//
//            it.addUpdateListener {
//                this.layoutParams.height = (it.animatedValue as Float).roundToInt()
//            }
//
//            it.start()
//        }
//    }
//
//    fun collapse(animationDurationMillis: Long) {
//
//        this.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        val height = this.height.toFloat()
//
//        ValueAnimator.ofFloat(height, 0f).let {
//            this.visibility = View.VISIBLE
//
//            it.duration = animationDurationMillis
//            it.interpolator = AccelerateInterpolator()
//
//            it.addUpdateListener {
//                this.layoutParams.height = (it.animatedValue as Float).roundToInt()
//            }
//
//            it.start()
//        }
//
//    }
}