package nk.sixstrings.ui.play

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable

class TabDrawable(private val context: Context, private val tab: String, private val progress: Float, private val drawableHeight: Float) : Drawable() {

    private val tabTextMarginToCanvasRatio = 0.382f

    private val tabTextPaint: Paint = Paint().apply {
        setARGB(255, 0, 0, 0)
        typeface = Typeface.createFromAsset(context.assets, "fonts/roboto_mono_light.ttf")
    }

    override fun draw(canvas: Canvas) {
        canvas.drawTab(tab, progress)
    }

    private fun Canvas.drawTab(tab: String, progress: Float) {

        val strings = tab.split("\n")
        val numberOfStrings = strings.size
        val longestString = strings.maxBy {
            it.length
        }

        val canvasHeight = bounds.height().toFloat()
        val marginSize = canvasHeight * tabTextMarginToCanvasRatio / 2
        tabTextPaint.textSize = (canvasHeight - marginSize * 2) / numberOfStrings

        val startX = bounds.width().toFloat()
        val endX = -tabTextPaint.measureText(longestString)
        val xPos = linearlyInterpolate(startX, endX, progress)

        tab.split("\n").forEachIndexed { index, string ->
            this.drawText(string,
                    xPos,
                    (tabTextPaint.textSize * (index + 1)) + marginSize,
                    tabTextPaint)
        }
    }

    private fun linearlyInterpolate(x0: Float, x1: Float, pos: Float): Float {
        return x0 + ((x1 - x0) * pos)
    }

    override fun setAlpha(alpha: Int) {
        // This method is required
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        // This method is required
    }

    override fun getOpacity(): Int =
    // Must be PixelFormat.UNKNOWN, TRANSLUCENT, TRANSPARENT, or OPAQUE
            PixelFormat.OPAQUE
}