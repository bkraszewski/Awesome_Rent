package pl.starter.android.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import pl.starter.android.service.*
import pl.starter.android.utils.SessionRepository
import pl.starter.android.utils.SessionRepositoryImpl
import javax.inject.Singleton

@Module
open class RepositoryProvidersModule {

    @Provides
    @Singleton
    fun provideSessionRepository(sharedPreferences: SharedPreferences): SessionRepository =
        SessionRepositoryImpl(sharedPreferences)

    @Provides
    @Singleton
    fun provideApiRepository(apiService: ApiService,
                             userRepository: UserRepository,
                             sessionRepository: SessionRepository): ApiRepository = ApiRepositoryImpl(apiService, userRepository, sessionRepository)

    @Provides
    @Singleton
    fun provideUserRepository(sharedPreferences: SharedPreferences): UserRepository {
        return UserRepositoryImpl(sharedPreferences)
    }

}
