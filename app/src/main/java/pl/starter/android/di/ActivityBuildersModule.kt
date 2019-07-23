package pl.starter.android.di


import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.starter.android.auth.AuthActivity
import pl.starter.android.main.MainActivity
import pl.starter.android.splash.SplashActivity

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeAuthActivity(): AuthActivity

}
