package pl.starter.android.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import pl.starter.android.service.ApiService
import pl.starter.android.service.ApiServiceImpl
import pl.starter.android.service.UuidGenerator
import pl.starter.android.service.UuidGeneratorImpl
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

    @Provides
    @Singleton
    fun provideFirebaseAuth() : FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideUuidGenerator(): UuidGenerator{
        return UuidGeneratorImpl()
    }

}
