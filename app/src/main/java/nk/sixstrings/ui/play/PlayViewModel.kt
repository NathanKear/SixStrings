package nk.sixstrings.ui.play

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import nk.sixstrings.models.Song
import nk.sixstrings.models.TabInfo
import nk.sixstrings.util.CombinedLiveData

class PlayViewModel : ViewModel() {

    val song = MutableLiveData<Song>()

    private val playProgress = MutableLiveData<Float>()
    val tabInfo = MutableLiveData<TabInfo>()
    val tabPlay = CombinedLiveData<Float, TabInfo, Pair<Float?, TabInfo?>>(playProgress, tabInfo) { playProgress, tabInfo -> Pair(playProgress, tabInfo) }


    init {
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
}
