package nk.sixstrings.ui.songlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import nk.sixstrings.infrastructure.SongListInteractor
import nk.sixstrings.models.Song

class SongListViewModel constructor(private val songListInteractor: SongListInteractor) : ViewModel() {

    val songs = MutableLiveData<List<Song>>()

    init {
        fun getSongsSuccess(data: List<Song>) {
            songs.postValue(data)
        }

        fun getSongsFailure(throwable: Throwable) {
            songs.postValue(emptyList())
        }

        songListInteractor.getSongs().subscribe(
                ::getSongsSuccess,
                ::getSongsFailure
        )
    }
}
