package nk.sixstrings.di

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/* Key used to associate ViewModel types with providers */
@MapKey
@Target(AnnotationTarget.FUNCTION)
annotation class ViewModelKey(
        val value: KClass<out ViewModel>
)