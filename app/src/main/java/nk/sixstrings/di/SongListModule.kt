package nk.sixstrings.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import nk.sixstrings.infrastructure.SongListInteractor
import nk.sixstrings.ui.songlist.SongListFragment
import nk.sixstrings.ui.songlist.SongListViewModel

/* Install module responsible for providing ViewModel into parent component */
@Module(includes = [
    SongListModule.ProvideViewModel::class
])
abstract class SongListModule {

    /* Install module into subcomponent to have access to bound fragment instance */
    @ContributesAndroidInjector(modules = [
        InjectViewModel::class
    ])
    abstract fun bind(): SongListFragment

    /* Module that uses bound fragment and provided factory uses ViewModelProviders
        to provide instance of SongListViewModel */
    @Module
    class InjectViewModel {

        @Provides
        fun provideSongListViewModel(
                factory: ViewModelProvider.Factory,
                target: SongListFragment
        ) = ViewModelProviders.of(target, factory).get(SongListViewModel::class.java)

    }

    @Module
    class ProvideViewModel {

        @Provides
        @IntoMap
        @ViewModelKey(SongListViewModel::class)
        fun provideSongListViewModel(songListInteractor: SongListInteractor): ViewModel =
                SongListViewModel(songListInteractor)

    }

}