package nk.sixstrings.infrastructure

import io.reactivex.Single
import nk.sixstrings.models.Song

class SongListInteractor(private val songRepository: SongRepository) {

    fun getSongs() : Single<List<Song>> {
        return songRepository.getSongs()
    }
}