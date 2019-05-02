package nk.sixstrings.ui.play

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import nk.sixstrings.models.Song
import nk.sixstrings.models.TabInfo
import nk.sixstrings.util.CombinedLiveData
import nk.sixstrings.util.extensions.clamp

class PlayViewModel : ViewModel() {

    sealed class PlayState {
        object Play : PlayState()
        object Stop : PlayState()
    }

    val song = MutableLiveData<Song>()
    val playState = MutableLiveData<PlayState>()
    val playProgress = MutableLiveData<Float>()
    val tabInfo = MutableLiveData<TabInfo>()
    val tabPlay = CombinedLiveData<Float, TabInfo, Pair<Float?, TabInfo?>>(playProgress, tabInfo) { playProgress, tabInfo -> Pair(playProgress, tabInfo) }

    init {
        playState.value = PlayState.Stop
        playProgress.value = 0.0f
        song.value = Song("Viðrar vel til loftárása", "Sigur Rós", "3fac9b17-802a-4e35-928b-cebaaa133566")
        tabInfo.value = TabInfo("""
                |----2---0-2-----|--0-------------|--------------|--0-----2-------|2--2--2-0-2-----|--0-------------|----------------|------|----2----(2)---(2)---(2)--------|----|
                |----0---0-0-0---|3---2-0---------|--0-----2-----|4---4---------0-|---0--0-0-0---3-|----2---0-------|----0-------2---|------|----0-----0-----0-----0---0--0--|0---|
                |----4---4-4-----|4-4-4-4-----4---|--2-----0-----|--0-----4---4---|---4--4-4-4---4-|--4-4---4-------|----2-------0---|------|----4-----4-----4-----4---2--2--|4---|
                |------------4---|4---4-4-----4---|--2-----2-----|4---4-----------|--------------4-|----4---4---4---|----2-------2---|----2-|----4--4--4--4--4--4--4---2--2--|4---|
                |--0---0-------0-|----------------|0---0-----0---|0-----0---0-----|-----0-0----0---|0-----0-------0-|--0---0---0-----|------|0-----0-----0-----0---------0-0-|----|
                |----------------|--------------0-|------0-----0-|----------------|----------------|----------------|0-------0-------|------|--0-----0-----0-----0---0-------|--0-|
            """)
    }

    fun setProgress(progress: Float) {

        if (progress >= 1f) stop()

        val progress = progress.clamp(0f, 1f)
        playProgress.value = progress
    }

    fun tick() {
        // if (playState.value == PlayState.Stop) return

        playProgress.value?.let {
            setProgress(it + 0.001f)
        }
    }

    fun play() {
        playState.value = PlayState.Play
    }

    fun stop() {
        playState.value = PlayState.Stop
    }
}
