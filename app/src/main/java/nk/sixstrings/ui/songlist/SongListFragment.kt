package nk.sixstrings.ui.songlist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.song_list_fragment.*

import nk.sixstrings.R
import nk.sixstrings.models.Song

class SongListFragment : Fragment() {

    companion object {
        fun newInstance() = SongListFragment()
    }

    private lateinit var viewModel: SongListViewModel
    private val songs = listOf(
            Song("Wish You Were Here", "Pink Floyd", "21484de1-6bcd-42d1-812e-600a29e1c473"),
            Song("Airbag", "Radiohead", "027070fd-6b26-411e-b7b8-245b80392cc1"),
            Song("Cycling Trivialities", "José González", "ae51910a-0fbf-4eae-9851-a68e9b99cabb"),
            Song("With The Ink Of A Ghost", "José González", "7f98b43f-43fb-49f7-b1c9-046a027f496f"),
            Song("Viðrar vel til loftárása", "Sigur Rós", "3fac9b17-802a-4e35-928b-cebaaa133566"),
            Song("Ágætis byrjun", "Sigur Rós", "698f58bf-41f5-4330-9801-3b94d8733a76"),
            Song("Perth", "Bon Iver", "28978164-6547-4cf4-a98a-07d2353162ae"),
            Song("Brokeback Mountain Theme", "Gustavo Santaolalla", "c488c941-38a4-465b-9a23-2ca8c1e57a55"),
            Song("Champagne Supernova", "Oasis", "ce3600cf-b430-494b-b6f8-bc82b03814ce"),
            Song("The Last Of Us Theme", "Gustavo Santaolalla", "6a43ba38-1a31-4a39-9722-d374756f5ca7"),
            Song("Caring Is Creepy", "The Shins", "344df1ca-59ec-4167-92f3-88fe8ec76175"),
            Song("New Slang", "The Shins", "5b8854db-a997-4a30-b656-fd9701cb379a"),
            Song("Feeling You", "Harrison Storm", "6edc165c-16f3-4335-ade4-ba3a7083a2c0"),
            Song("Mean To Me", "Stella Donnelly", "699eae38-a210-4274-85a7-3ebb6970beec"),
            Song("The Barrel", "Aldous Harding", "5699a096-7bfe-45dc-93e0-d17b03976ede"),
            Song("Chasing Gold", "Dustin Tebbutt", "0127e0d6-baa9-423b-9692-92027fa6827c"),
            Song("Broke Machine", "Tori Forsyth", "3d17f651-1da1-44ce-b3e3-00f3a82c04b0")
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
            it.adapter = SongListRecyclerViewAdapter(songs, ::onSongItemClicked, context!!)
        }
    }

    private fun onSongItemClicked(song: Song) {
        SongListFragmentDirections.actionSongListFragmentToPlayFragment().apply {
            songId = song.id
            findNavController().navigate(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SongListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
