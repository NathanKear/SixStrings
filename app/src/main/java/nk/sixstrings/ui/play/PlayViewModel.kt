package nk.sixstrings.ui.play

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import nk.sixstrings.infrastructure.PlayInteractor
import nk.sixstrings.models.Song
import nk.sixstrings.models.TabInfo
import nk.sixstrings.util.CombinedLiveData
import nk.sixstrings.util.extensions.clamp
import java.util.concurrent.TimeUnit

class PlayViewModel(private val playInteractor: PlayInteractor) : ViewModel() {

    sealed class PlayState {
        object Play : PlayState()
        object Pause : PlayState()
        object Finished : PlayState()
        object DraggedWhilePaused : PlayState()
        object DraggedWhilePlaying : PlayState()
    }

    private val startProgress = 0.0f
    private val endProgress = 1.0f
    private val progressTickStep = 0.001f

    val song = MutableLiveData<Song>()
    val playState = MutableLiveData<PlayState>()
    val playProgress = MutableLiveData<Float>()
    val tabInfo = MutableLiveData<TabInfo>()
    val tabPlay = CombinedLiveData<Float, TabInfo, Pair<Float?, TabInfo?>>(playProgress, tabInfo) { playProgress, tabInfo -> Pair(playProgress, tabInfo) }
    private val timer = Observable.interval(17, TimeUnit.MILLISECONDS)

    init {
        playState.value = PlayState.Pause
        playProgress.value = startProgress

        fun tick(i: Long) {

            if (playState.value != PlayState.Play) return

            playProgress.value?.let {
                setProgress(it + progressTickStep)
            }
        }

        timer.observeOn(AndroidSchedulers.mainThread()).subscribe(::tick)
    }

    fun loadSong(songId: String) {

        fun getTabInfoSuccess(data: TabInfo) {
            tabInfo.value = data
        }

        playInteractor.getTabInfo(songId).subscribe(::getTabInfoSuccess)

        fun getSongSuccess(data: Song) {
            song.value = data
        }

        playInteractor.getSong(songId).subscribe(::getSongSuccess)
    }

    fun setProgress(progress: Float) {

        if (progress >= endProgress && (playState.value == PlayState.Play || playState.value == PlayState.Pause)) {
            playState.value = PlayState.Finished
        }

        val progress = progress.clamp(startProgress, endProgress)
        playProgress.postValue(progress)
    }

    fun play() {
        playState.value = PlayState.Play
    }

    fun pause() {
        playState.value = PlayState.Pause
    }

    fun replay() {
        playState.value = PlayState.Play
        playProgress.value = startProgress
    }

    fun dragProgress() {
        when (playState.value) {
            PlayState.Play -> playState.value = PlayState.DraggedWhilePlaying
            PlayState.Pause, PlayState.Finished -> playState.value = PlayState.DraggedWhilePaused
        }
    }

    fun releaseProgress() {
        when (playState.value) {
            PlayState.DraggedWhilePlaying -> playState.value = PlayState.Play
            PlayState.DraggedWhilePaused -> playState.value = PlayState.Pause
        }
    }
}
