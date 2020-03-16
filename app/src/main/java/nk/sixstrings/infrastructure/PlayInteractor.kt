package nk.sixstrings.infrastructure

import io.reactivex.Single
import nk.sixstrings.models.Song
import nk.sixstrings.models.TabInfo
import javax.inject.Inject

class PlayInteractor @Inject constructor(private val songRepository: SongRepository) {

    fun getSong(songId: String) : Single<Song> {
        return songRepository.getSong(songId)
    }

    fun getTabInfo(songId: String) : Single<TabInfo> {
        return songRepository.getTabInfo(songId)
    }
}