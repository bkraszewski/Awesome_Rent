package pl.starter.android.di


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pl.starter.android.feature.auth.AuthViewModel
import pl.starter.android.feature.auth.login.LoginViewModel
import pl.starter.android.feature.auth.register.RegisterViewModel
import pl.starter.android.feature.apartment.ApartmentViewModel
import pl.starter.android.feature.explore.ExploreViewModel
import pl.starter.android.feature.explore.list.RentListViewModel
import pl.starter.android.feature.explore.map.RentMapViewModel

import pl.starter.android.feature.main.MainViewModel
import pl.starter.android.feature.profile.ProfileViewModel
import pl.starter.android.feature.profile.UserListViewModel
import pl.starter.android.feature.splash.SplashViewModel

@Module
internal abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: BaseViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashViewModel::class)
    abstract fun bindSplashModel(viewModel: SplashViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthModel(viewModel: AuthViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    abstract fun bindProfileViewModel(viewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ExploreViewModel::class)
    abstract fun bindExploreViewModel(viewModel: ExploreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RentListViewModel::class)
    abstract fun bindRentListViewModel(viewModel: RentListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RentMapViewModel::class)
    abstract fun bindRentMapViewModel(viewModel: RentMapViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ApartmentViewModel::class)
    abstract fun bindEditCreateApartmentViewModel(viewModel: ApartmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserListViewModel::class)
    abstract fun bindEditUserListViewModel(viewModel: UserListViewModel): ViewModel

}
