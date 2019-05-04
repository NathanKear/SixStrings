package nk.sixstrings.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import nk.sixstrings.App
import javax.inject.Singleton

@Component(modules = [
    AppModule::class,
    SongListModule::class,
    PlayModule::class
])
@Singleton
interface AppComponent {
    fun inject(app: App)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: App): Builder

        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }
}