package nk.sixstrings.infrastructure

import io.reactivex.Single
import nk.sixstrings.models.Song
import javax.inject.Inject

class SongListInteractor @Inject constructor (private val songRepository: SongRepository) {

    fun getSongs() : Single<List<Song>> {
        return songRepository.getSongs()
    }
}