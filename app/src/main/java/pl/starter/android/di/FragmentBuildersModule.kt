package pl.starter.android.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.starter.android.feature.auth.login.LoginFragment

@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

}
