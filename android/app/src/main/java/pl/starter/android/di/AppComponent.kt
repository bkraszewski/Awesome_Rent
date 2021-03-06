package pl.starter.android.di

import androidx.lifecycle.ViewModelProvider
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import pl.starter.android.base.BaseApp
import javax.inject.Singleton

@Singleton
@Component(
    modules = arrayOf(
        AppModule::class,
        ActivityBuildersModule::class,
        ViewModelModule::class,
        UseCaseProvidersModule::class,
        RepositoryProvidersModule::class,
        ServiceProviderModule::class,
        AndroidSupportInjectionModule::class
    )
)
interface AppComponent {
    fun inject(app: BaseApp)
    fun getViewModelProviderFactory(): ViewModelProvider.Factory
}
