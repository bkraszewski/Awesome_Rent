package pl.starter.android.di


import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.starter.android.feature.auth.AuthActivity
import pl.starter.android.feature.edit_create_apartment.EditCreateApartmentActivity
import pl.starter.android.feature.main.MainActivity
import pl.starter.android.feature.splash.SplashActivity

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeAuthActivity(): AuthActivity

    abstract fun contributeEditCreateApartmentActivity(): EditCreateApartmentActivity

}
