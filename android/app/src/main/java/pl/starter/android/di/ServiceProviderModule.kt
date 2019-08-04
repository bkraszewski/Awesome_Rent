package pl.starter.android.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import pl.starter.android.service.ApiService
import pl.starter.android.service.FirebaseApiServiceImpl
import pl.starter.android.service.UuidGenerator
import pl.starter.android.service.UuidGeneratorImpl
import javax.inject.Singleton

@Module
open class ServiceProviderModule {

    @Provides
    @Singleton
    fun provideApiService(
        firebaseAuth: FirebaseAuth): ApiService {
        //todo replace later with actual implementation
        return FirebaseApiServiceImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideUuidGenerator(): UuidGenerator {
        return UuidGeneratorImpl()
    }

}
