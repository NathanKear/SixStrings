package nk.sixstrings.ui.songlist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.song_list_fragment.*

import nk.sixstrings.R
import nk.sixstrings.models.Song
import javax.inject.Inject

class SongListFragment : Fragment() {

    companion object {
        fun newInstance() = SongListFragment()
    }

    @Inject
    lateinit var vm: SongListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.song_list_fragment, container, false)
    }

    private fun onSongItemClicked(song: Song) {
        SongListFragmentDirections.actionSongListFragmentToPlayFragment().apply {
            songId = song.id
            findNavController().navigate(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vm.songs.observe(this, Observer { songs ->
            song_list_recycler_view.let {
                it.layoutManager = LinearLayoutManager(context)
                it.adapter = SongListRecyclerViewAdapter(songs, ::onSongItemClicked, context!!)
            }
        })
    }

}
