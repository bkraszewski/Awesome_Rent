package pl.starter.android.di


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

import pl.starter.android.main.MainViewModel

@Module
internal abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: BaseViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainModel(viewModel: MainViewModel): ViewModel


}
