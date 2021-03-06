package nk.sixstrings.ui.play

import android.app.Activity.*
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
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

        vm.loadSong(songId)

        advance_options_collapsible.collapse()

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

        play_speed.setOnClickListener {
            vm.toggleSpeedDialog()
        }

        play_difficulty.setOnClickListener {
            vm.toggleDifficultyDialog()
        }
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
                    advance_options_collapsible.expand(300)
                }
                PlayViewModel.OptionsMenuState.Hide -> {
                    hide_advance_play_options.apply {
                        text = "Show Options..."
                    }
                    advance_options_collapsible.collapse(300)
                }
            }
        })

        vm.speedToggleDialogStage.observe(this, Observer {
            when(it) {
                PlayViewModel.SpeedToggleDialogState.Show -> {
                    val playSpeedPickerDialog = PlaySpeedPickerDialog()
                    playSpeedPickerDialog.setTargetFragment(this, PLAY_SPEED_DIALOG_REQUEST_CODE)
                    playSpeedPickerDialog.show(requireFragmentManager(), "dialog") // TODO: Turn string into const
                }
            }
        })

        vm.difficultyToggleDialogStage.observe(this, Observer {
            when(it) {
                PlayViewModel.DifficultyToggleDialogState.Show -> {
                    val playDifficultyPickerDialog = PlayDifficultyPickerDialog()
                    playDifficultyPickerDialog.setTargetFragment(this, PLAY_DIFFICULTY_DIALOG_REQUEST_CODE)
                    playDifficultyPickerDialog.show(requireFragmentManager(), "dialog") // TODO: Turn string into const
                }
            }
        })

        vm.playSpeed.observe(this, Observer {
            play_speed.text = when(it) {
                PlayViewModel.PlaySpeed.Slow -> "Slow"
                PlayViewModel.PlaySpeed.Medium -> "Medium"
                PlayViewModel.PlaySpeed.Fast -> "Fast"
            }
        })

        vm.playDifficulty.observe(this, Observer {
            play_difficulty.text = when(it) {
                PlayViewModel.PlayDifficulty.Easy -> "Easy"
                PlayViewModel.PlayDifficulty.Medium -> "Medium"
                PlayViewModel.PlayDifficulty.Hard -> "Hard"
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) return

        when (requestCode) {
            PLAY_SPEED_DIALOG_REQUEST_CODE -> {
                // TODO: Remove magic strings
                val playSpeed: PlayViewModel.PlaySpeed = when(data?.extras?.getString(PlaySpeedPickerDialog.SELECTED_PLAY_SPEED)) {
                    "Slow" -> PlayViewModel.PlaySpeed.Slow
                    "Medium" -> PlayViewModel.PlaySpeed.Medium
                    "Fast" -> PlayViewModel.PlaySpeed.Fast
                    else -> PlayViewModel.PlaySpeed.Medium
                }

                vm.setPlaySpeed(playSpeed)
            }
            PLAY_DIFFICULTY_DIALOG_REQUEST_CODE -> {
                // TODO: Remove magic strings
                val playDifficulty: PlayViewModel.PlayDifficulty = when(data?.extras?.getString(PlayDifficultyPickerDialog.SELECTED_PLAY_DIFFICULTY)) {
                    "Easy" -> PlayViewModel.PlayDifficulty.Easy
                    "Medium" -> PlayViewModel.PlayDifficulty.Medium
                    "Hard" -> PlayViewModel.PlayDifficulty.Hard
                    else -> PlayViewModel.PlayDifficulty.Medium
                }

                vm.setPlayDifficulty(playDifficulty)
            }
        }
    }

    companion object {
        const val PLAY_SPEED_DIALOG_REQUEST_CODE = 1001
        const val PLAY_DIFFICULTY_DIALOG_REQUEST_CODE = 1002
    }
}
