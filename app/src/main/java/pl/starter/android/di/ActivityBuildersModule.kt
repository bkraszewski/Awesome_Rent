package pl.starter.android.di


import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.starter.android.main.MainActivity

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity

}
