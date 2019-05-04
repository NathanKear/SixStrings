package nk.sixstrings.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import nk.sixstrings.infrastructure.PlayInteractor
import nk.sixstrings.ui.play.PlayFragment
import nk.sixstrings.ui.play.PlayViewModel


/* Install module responsible for providing ViewModel into parent component */
@Module(includes = [
    PlayModule.ProvideViewModel::class
])
abstract class PlayModule {

    /* Install module into subcomponent to have access to bound fragment instance */
    @ContributesAndroidInjector(modules = [
        InjectViewModel::class
    ])
    abstract fun bind(): PlayFragment

    /* Module that uses bound fragment and provided factory uses ViewModelProviders
        to provide instance of SongListViewModel */
    @Module
    class InjectViewModel {

        @Provides
        fun providePlayViewModel(
                factory: ViewModelProvider.Factory,
                target: PlayFragment
        ) = ViewModelProviders.of(target, factory).get(PlayViewModel::class.java)

    }

    @Module
    class ProvideViewModel {

        @Provides
        @IntoMap
        @ViewModelKey(PlayViewModel::class)
        fun providePlayViewModel(playInteractor: PlayInteractor): ViewModel =
                PlayViewModel(playInteractor)

    }

}