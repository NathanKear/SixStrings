package nk.sixstrings.ui.songlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_row.view.*
import nk.sixstrings.R

class SongListRecyclerViewAdapter(private val songs: List<Song>, private val context: Context) : RecyclerView.Adapter<SongListRecyclerViewAdapter.SongViewHolder>() {

    override fun getItemCount() = songs.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) = holder.bind(songs[position])

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val inflater = LayoutInflater.from(context)
        return SongViewHolder(inflater.inflate(R.layout.recyclerview_row, parent, false))
    }

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(song: Song) = with(itemView) {
            song_name.text = song.name
            song_artist.text = song.artist
        }
    }
}