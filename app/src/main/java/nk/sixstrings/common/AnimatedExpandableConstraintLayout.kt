package nk.sixstrings.common

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.Transformation
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart


class AnimatedExpandableConstraintLayout(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    private sealed class CurrentState {
        object Expanded : CurrentState()
        object Collapsed : CurrentState()
    }

    private var currentState: CurrentState = CurrentState.Collapsed

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        this.currentState = CurrentState.Collapsed
        this.visibility = View.INVISIBLE
        this.layoutParams.height = 1
    }

    private fun getDesiredExpandedHeight(): Int {
        this.measure(
                MeasureSpec.makeMeasureSpec(this.width, MeasureSpec.EXACTLY) /* Irrelevant, only care about measuring height */,
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )

        return this.measuredHeight
    }

    fun expand(durationMillis: Long = 0) {

        if (this.currentState == CurrentState.Expanded) return
        this.currentState = CurrentState.Expanded

        val targetHeight = getDesiredExpandedHeight()

        ValueAnimator.ofInt(1, targetHeight).let {
            it.duration = durationMillis
            it.interpolator = AccelerateInterpolator()

            it.doOnStart {
                this.visibility = View.VISIBLE
                this.layoutParams.height = 1
                this.requestLayout()
                Log.d("Expand", "Start")
            }

            it.doOnEnd {
                this.layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                this.requestLayout()
                Log.d("Expand", "End")
            }

            it.addUpdateListener {
                this.layoutParams.height = it.animatedValue as Int
                this.requestLayout()
                Log.d("Expand", this.layoutParams.height.toString())
            }

            it.start()
        }
    }

    fun collapse(durationMillis: Long = 0) {

        if (this.currentState == CurrentState.Collapsed) return
        this.currentState = CurrentState.Collapsed

        val initialHeight = this.measuredHeight

        ValueAnimator.ofInt(initialHeight, 1).let {
            it.duration = durationMillis
            it.interpolator = AccelerateInterpolator()

            it.doOnStart {
                Log.d("Collapse", "Start")
                this.requestLayout()
            }

            it.doOnEnd {
                this.layoutParams.height = 1
                this.visibility = View.INVISIBLE
                Log.d("Collapse", "End")
                this.requestLayout()
            }

            it.addUpdateListener {
                this.layoutParams.height = it.animatedValue as Int
                this.requestLayout()
                Log.d("Collapse", this.layoutParams.height.toString())
            }

            it.start()
        }
    }
}