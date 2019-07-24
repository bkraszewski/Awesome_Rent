package pl.starter.android.di

import dagger.Module
import dagger.Provides
import pl.starter.android.service.ApiService
import pl.starter.android.service.ApiServiceImpl
import pl.starter.android.utils.BaseSchedulers
import javax.inject.Singleton

@Module
open class ServiceProviderModule {

    @Provides
    @Singleton
    fun provideApiService(baseSchedulers: BaseSchedulers): ApiService{
        //todo replace later with actual implementation
        return ApiServiceImpl(baseSchedulers)
    }

}
