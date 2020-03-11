package nk.sixstrings.infrastructure

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloMutationCall
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import nk.sixstrings.SongListQuery
import nk.sixstrings.models.Song
import nk.sixstrings.models.TabInfo
import nk.sixstrings.util.extensions.toSingle
import nk.sixstrings.util.extensions.unwrapResponseAsError
import javax.inject.Inject

class SongRepository @Inject constructor(private val apolloClient: ApolloClient) {

    private val db by lazy { FirebaseFirestore.getInstance() }

    private val tab = TabInfo(songId = "3fac9b17-802a-4e35-928b-cebaaa133566", tab = """
                |----2---0-2-----|--0-------------|--------------|--0-----2-------|2--2--2-0-2-----|--0-------------|----------------|------|----2----(2)---(2)---(2)--------|----|
                |----0---0-0-0---|3---2-0---------|--0-----2-----|4---4---------0-|---0--0-0-0---3-|----2---0-------|----0-------2---|------|----0-----0-----0-----0---0--0--|0---|
                |----4---4-4-----|4-4-4-4-----4---|--2-----0-----|--0-----4---4---|---4--4-4-4---4-|--4-4---4-------|----2-------0---|------|----4-----4-----4-----4---2--2--|4---|
                |------------4---|4---4-4-----4---|--2-----2-----|4---4-----------|--------------4-|----4---4---4---|----2-------2---|----2-|----4--4--4--4--4--4--4---2--2--|4---|
                |--0---0-------0-|----------------|0---0-----0---|0-----0---0-----|-----0-0----0---|0-----0-------0-|--0---0---0-----|------|0-----0-----0-----0---------0-0-|----|
                |----------------|--------------0-|------0-----0-|----------------|----------------|----------------|0-------0-------|------|--0-----0-----0-----0---0-------|--0-|
            """.trimIndent())

    fun getSongs() : Single<List<Song>> {
        return apolloClient.query(SongListQuery())
                .toSingle()
                .unwrapResponseAsError().map { data ->
                    data.songs.map {
                        Song(it.name, it.artist, it.id)
                    }
                }
    }

    fun getTabInfo(songId: String) : Single<TabInfo> {

        return Single.create { subscriber ->
            db.collection("tabs")
                    .whereEqualTo("songId", songId)
                    .get()
                    .addOnSuccessListener { result ->

                        val song = result.documents[0].toObject(TabInfo::class.java)

                        if (song != null) {
                            subscriber.onSuccess(song)
                        } else {
                            subscriber.onError(FirestoreObjectMappingException("Could not map object ${result.documents[0].id} => ${result.documents[0].data} to ${TabInfo::class.qualifiedName}"))
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("SongRepository", exception.localizedMessage)
                        subscriber.onError(exception)
                    }
        }
    }

    fun getSong(songId: String) : Single<Song> {
        return Single.create { subscriber ->
            db.collection("songs")
                    .document(songId)
                    .get()
                    .addOnSuccessListener { result ->

                        Log.d("SongRepository", "${result.id} => ${result.data}")

                        val song = result.toSong()

                        if (song != null)
                            subscriber.onSuccess(song)
                        else
                            subscriber.onError(FirestoreObjectMappingException("Could not map object ${result.id} => ${result.data} to ${Song::class.qualifiedName}"))
                    }
                    .addOnFailureListener { exception ->
                        Log.e("SongRepository", exception.localizedMessage)
                        subscriber.onError(exception)
                    }
        }
    }

    private fun Song.toPersistenceObject() = HashMap<String, String>().also {
        it["name"] = this.name
        it["artist"] = this.artist
    }

    private fun DocumentSnapshot.toSong() : Song? {

        if (this.data == null
                || this.data!!["name"] == null
                || this.data!!["artist"] == null) {
            return null
        }

        return Song(
                this.data!!["name"].toString(),
                this.data!!["artist"].toString(),
                this.id
        )
    }
//
//    private fun DocumentSnapshot.toTabInfo() : TabInfo? {
//
//        if (this.data == null
//                || this.data!!["name"] == null
//                || this.data!!["artist"] == null) {
//            return null
//        }
//
//
//    }

    class FirestoreObjectMappingException(message: String) : Exception(message)
}