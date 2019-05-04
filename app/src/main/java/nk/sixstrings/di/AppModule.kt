package nk.sixstrings.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import nk.sixstrings.infrastructure.PlayInteractor
import nk.sixstrings.infrastructure.SongListInteractor
import nk.sixstrings.infrastructure.SongRepository
import javax.inject.Provider
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    fun provideSongRepository() = SongRepository()

    @Provides
    fun provideSongListInteractor(songRepository: SongRepository) = SongListInteractor(songRepository)

    @Provides
    fun providePlayInteractor(songRepository: SongRepository) = PlayInteractor(songRepository)

    /* Singleton factory that searches generated map for specific provider and
        uses it to get a ViewModel instance */
    @Provides
    @Singleton
    fun provideViewModelFactory(
            providers: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
    ) = object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return requireNotNull(providers[modelClass as Class<out ViewModel>]).get() as T
        }
    }
}