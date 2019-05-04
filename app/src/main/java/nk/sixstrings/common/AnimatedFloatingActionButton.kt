package nk.sixstrings.common

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.core.animation.doOnEnd
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AnimatedFloatingActionButton(context: Context, attrs: AttributeSet) : FloatingActionButton(context, attrs) {

    var imageResourceId: Int? = null

    @Override
    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)

        imageResourceId = resId
    }

    fun animateToNextImage(nextImage: Int, animationDurationMillis: Long, skipAnimationIfNoChange: Boolean = true) {

        if(imageResourceId == nextImage && skipAnimationIfNoChange) return

        ValueAnimator.ofFloat(1f, 0f).let {
            it.duration = animationDurationMillis / 2
            it.interpolator = AccelerateInterpolator()
            it.doOnEnd {
                this.setImageResource(nextImage)

                ValueAnimator.ofFloat(0f, 1f).let {
                    it.duration = animationDurationMillis / 2
                    it.interpolator = DecelerateInterpolator()

                    it.addUpdateListener {
                        this.scaleX = it.animatedValue as Float
                        this.scaleY = it.animatedValue as Float
                    }

                    it.start()
                }
            }

            it.addUpdateListener {
                this.scaleX = it.animatedValue as Float
                this.scaleY = it.animatedValue as Float
            }

            it.start()
        }
    }
}