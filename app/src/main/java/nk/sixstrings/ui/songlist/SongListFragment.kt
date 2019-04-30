package nk.sixstrings.ui.songlist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.song_list_fragment.*

import nk.sixstrings.R

class SongListFragment : Fragment() {

    companion object {
        fun newInstance() = SongListFragment()
    }

    private lateinit var viewModel: SongListViewModel
    private val songs = listOf(
        Song("Feeling You", "Harrison Storm"),
        Song("Mean To Me", "Stella Donnelly"),
        Song("The Barrel", "Aldous Harding"),
        Song("Chasing Gold", "Dustin Tebbutt"),
        Song("Broke Machine", "Tori Forsyth")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.song_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        song_list_recycler_view.let {
            it.layoutManager = LinearLayoutManager(context)
            it.adapter = SongListRecyclerViewAdapter(songs, context!!)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SongListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
