package nk.sixstrings.ui.play

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.play_fragment.*

import nk.sixstrings.R
import nk.sixstrings.models.Song
import nk.sixstrings.models.TabInfo
import nk.sixstrings.util.OrdinalNumberSuffix

class PlayFragment : Fragment() {

    companion object {
        fun newInstance() = PlayFragment()
    }

    private lateinit var viewModel: PlayViewModel
    private var playProgress: Float = 0.0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.play_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val songId = PlayFragmentArgs.fromBundle(requireArguments()).songId
        // TODO: Use the ViewModel

        viewModel = ViewModelProviders.of(this).get(PlayViewModel::class.java)

        viewModel.song.observe(this, Observer {
            updateSong(it)
        })

        viewModel.tabInfo.observe(this, Observer {
            updateTabInfo(it)
        })

        viewModel.tabPlay.observe(this, Observer {
            val playProgress = it.first
            val tabInfo = it.second

            if (playProgress != null && tabInfo != null) {
                updateTab(tabInfo, playProgress)
            }
        })

        play_progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                playProgress = progress / 1000f
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    private fun updateSong(song: Song) {
        song_name.text = song.name
        song_artist.text = song.artist
    }

    private fun updateTabInfo(tabInfo: TabInfo) {
        tuning.text = tabInfo.tuning
        capo.text = if (tabInfo.capo == 0) {
            "No Capo"
        } else {
            "Capo ${OrdinalNumberSuffix.appendOrdinalSuffix(tabInfo.capo)} Fret"
        }
    }

    private fun updateTab(tabInfo: TabInfo, playProgress: Float) {
        val tabDrawing = TabDrawable(tabInfo.tab, playProgress)
        song_tab.setImageDrawable(tabDrawing)
    }

    class TabDrawable(private val tab: String, private val progress: Float) : Drawable() {
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

            canvas.drawTab(tab, progress)
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
