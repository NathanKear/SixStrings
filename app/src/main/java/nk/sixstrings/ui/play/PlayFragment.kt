package nk.sixstrings.ui.play

import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import androidx.core.animation.doOnEnd
import androidx.lifecycle.Observer
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.play_fragment.*

import nk.sixstrings.R
import nk.sixstrings.models.Song
import nk.sixstrings.models.TabInfo
import nk.sixstrings.ui.songlist.SongListViewModel
import nk.sixstrings.util.OrdinalNumberSuffix
import javax.inject.Inject
import kotlin.math.roundToInt

class PlayFragment : Fragment() {

    companion object {
        fun newInstance() = PlayFragment()
    }

    @Inject
    lateinit var vm: PlayViewModel

    private var playProgress: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.play_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val handler = Handler()
        val runnable: Runnable = object: Runnable {
            override fun run() {
                vm.tick()
                handler.postDelayed(this, 16)
            }
        }
        handler.postDelayed(runnable, 16)

        val songId = PlayFragmentArgs.fromBundle(requireArguments()).songId

        vm.loadSong(songId)

        vm.song.observe(this, Observer {
            updateSong(it)
        })

        vm.tabInfo.observe(this, Observer {
            updateTabInfo(it)
        })

        vm.tabPlay.observe(this, Observer {
            val playProgress = it.first
            val tabInfo = it.second

            if (playProgress != null && tabInfo != null) {
                updateTab(tabInfo, playProgress)
            }
        })

        vm.playProgress.observe(this, Observer {
            play_progress.progress = (it * 1000).roundToInt()
        })

        play_progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                if (!fromUser) return

                playProgress = progress / 1000f
                vm.setProgress(playProgress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                vm.stop()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                vm.play()
            }
        })

        vm.playState.observe(this, Observer {

            if (it is PlayViewModel.PlayState.Play) {

                play_pause_button.setOnClickListener { view ->
                    vm.stop()

                    ValueAnimator.ofFloat(1f, 0f).apply {
                        duration = 105L
                        interpolator = AccelerateInterpolator()
                        doOnEnd {
                            play_pause_button.setImageResource(android.R.drawable.ic_media_play)

                            ValueAnimator.ofFloat(0f, 1f).apply {
                                duration = 105L
                                interpolator = DecelerateInterpolator()

                                addUpdateListener {
                                    play_pause_button.scaleX = it.animatedValue as Float
                                    play_pause_button.scaleY = it.animatedValue as Float
                                }

                                start()
                            }
                        }

                        addUpdateListener {
                            play_pause_button.scaleX = it.animatedValue as Float
                            play_pause_button.scaleY = it.animatedValue as Float
                        }

                        start()
                    }
                }
            } else if (it is PlayViewModel.PlayState.Stop) {

                play_pause_button.setOnClickListener { view ->
                    vm.play()

                    ValueAnimator.ofFloat(1f, 0f).apply {
                        duration = 105L
                        interpolator = AccelerateInterpolator()
                        doOnEnd {
                            play_pause_button.setImageResource(android.R.drawable.ic_media_pause)

                            ValueAnimator.ofFloat(0f, 1f).apply {
                                duration = 105L
                                interpolator = DecelerateInterpolator()

                                addUpdateListener {
                                    play_pause_button.scaleX = it.animatedValue as Float
                                    play_pause_button.scaleY = it.animatedValue as Float
                                }

                                start()
                            }
                        }

                        addUpdateListener {
                            play_pause_button.scaleX = it.animatedValue as Float
                            play_pause_button.scaleY = it.animatedValue as Float
                        }

                        start()
                    }
                }
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
            textSize = 100f
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
