package nk.sixstrings.ui.play

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.play_fragment.*

import nk.sixstrings.R

class PlayFragment : Fragment() {

    companion object {
        fun newInstance() = PlayFragment()
    }

    private lateinit var viewModel: PlayViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.play_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(PlayViewModel::class.java)

        song_id.text = PlayFragmentArgs.fromBundle(requireArguments()).songId
        // TODO: Use the ViewModel

        val tabDrawing = TabDrawable()
        song_tab.setImageDrawable(tabDrawing)
    }

    class TabDrawable : Drawable() {
        private val redPaint: Paint = Paint().apply { setARGB(255, 255, 0, 0) }
        private val tabTextPaint: Paint = Paint().apply {
            setARGB(255, 0, 0, 0)
            textSize = 50f
            typeface = Typeface.MONOSPACE
        }

        override fun draw(canvas: Canvas) {


            val tab = """
                |----2---0-2-----|--0-------------|--------------|--0-----2-------|2--2--2-0-2-----|--0-------------|----------------|------|----2----(2)---(2)---(2)--------|----|
                |----0---0-0-0---|3---2-0---------|--0-----2-----|4---4---------0-|---0--0-0-0---3-|----2---0-------|----0-------2---|------|----0-----0-----0-----0---0--0--|0---|
                |----4---4-4-----|4-4-4-4-----4---|--2-----0-----|--0-----4---4---|---4--4-4-4---4-|--4-4---4-------|----2-------0---|------|----4-----4-----4-----4---2--2--|4---|
                |------------4---|4---4-4-----4---|--2-----2-----|4---4-----------|--------------4-|----4---4---4---|----2-------2---|----2-|----4--4--4--4--4--4--4---2--2--|4---|
                |--0---0-------0-|----------------|0---0-----0---|0-----0---0-----|-----0-0----0---|0-----0-------0-|--0---0---0-----|------|0-----0-----0-----0---------0-0-|----|
                |----------------|--------------0-|------0-----0-|----------------|----------------|----------------|0-------0-------|------|--0-----0-----0-----0---0-------|--0-|
            """.trimIndent()

            canvas.drawTab(tab, 0.5f)
        }

        fun Canvas.drawTab(tab: String, progress: Float) {

            val strings = tab.split("\n")
            val longestString = strings.maxBy {
                it.length
            }

            val startX = bounds.width().toFloat()
            val endX = -tabTextPaint.measureText(longestString)
            val xPos = linearlyInterpolate(startX, endX, progress)

            tab.split("\n").forEachIndexed { index, string ->
                this.drawText(string,
                        xPos,
                        tabTextPaint.textSize * index,
                        tabTextPaint)
            }
        }

        fun linearlyInterpolate(x0: Float, x1: Float, pos: Float): Float {
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

}
