package nk.sixstrings.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.apollographql.apollo.ApolloClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Provider
import javax.inject.Singleton

@Module
class AppModule {

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

    @Provides
    @Singleton
    fun provideApolloClient(client: OkHttpClient) = ApolloClient
        .builder()
        .serverUrl("http://10.0.2.2:4000/")
        .okHttpClient(client)
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) = OkHttpClient
        .Builder()
        .readTimeout(5000, TimeUnit.MILLISECONDS)
        .writeTimeout(10000, TimeUnit.MILLISECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .build()

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
        .setLevel(HttpLoggingInterceptor.Level.BODY)
}