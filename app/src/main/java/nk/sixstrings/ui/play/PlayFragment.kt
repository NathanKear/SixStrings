package nk.sixstrings.ui.play

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintSet.WRAP_CONTENT
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.play_fragment.*
import kotlinx.android.synthetic.main.play_fragment.view.*
import nk.sixstrings.R
import nk.sixstrings.models.Song
import nk.sixstrings.models.TabInfo
import nk.sixstrings.util.OrdinalNumberSuffix
import javax.inject.Inject
import kotlin.math.roundToInt

class PlayFragment : Fragment() {

    @Inject
    lateinit var vm: PlayViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.play_fragment, container, false).apply {
            play_pause_button.setImageResource(R.drawable.ic_play_arrow_white_24dp)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val songId = PlayFragmentArgs.fromBundle(requireArguments()).songId

        play_speed.text = "Fast"
        play_difficulty.text = "Easy"

//        @SuppressLint("ObsoleteSdkInt")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            advance_options_collapsible.let {
//                it.layoutTransition
//                    .enableTransitionType(LayoutTransition.CHANGING)
//                it.layoutTransition.setDuration(300)
//            }
//        }

        vm.loadSong(songId)

        observeViewControls()
        observeViewModel()
    }

    private fun observeViewControls() {
        hide_advance_play_options.setOnClickListener {
            vm.toggleOptionsMenu()
        }

        play_progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                if (!fromUser) return

                vm.setProgress(progress / 1000f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                vm.dragProgress()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                vm.releaseProgress()
            }
        })
    }

    private fun observeViewModel() {

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

        vm.playState.observe(this, Observer {

            when (it) {
                is PlayViewModel.PlayState.Play -> {
                    play_pause_button.animateToNextImage(R.drawable.ic_pause_white_24dp, 200)

                    play_pause_button.setOnClickListener {
                        vm.pause()
                    }
                }
                is PlayViewModel.PlayState.Pause -> {
                    play_pause_button.animateToNextImage(R.drawable.ic_play_arrow_white_24dp, 200)

                    play_pause_button.setOnClickListener {
                        vm.play()
                    }
                }
                is PlayViewModel.PlayState.Finished -> {
                    play_pause_button.animateToNextImage(R.drawable.ic_replay_white_24dp, 200)

                    play_pause_button.setOnClickListener {
                        vm.replay()
                    }
                }
            }
        })

        vm.optionsMenuState.observe(this, Observer {



            when(it) {
                PlayViewModel.OptionsMenuState.Show -> {
                    hide_advance_play_options.apply {
                        text = "Hide Options..."
                    }
                    advance_options_collapsible.expand()
                }
                PlayViewModel.OptionsMenuState.Hide -> {
                    hide_advance_play_options.apply {
                        text = "Show Options..."
                    }
                    advance_options_collapsible.collapse()
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
        val tabDrawing = TabDrawable(requireContext(), tabInfo.tab, playProgress, song_tab.height.toFloat())
        song_tab.setImageDrawable(tabDrawing)
    }
}
