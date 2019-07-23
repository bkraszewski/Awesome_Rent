package pl.starter.android.di

import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import pl.starter.android.utils.SessionRepository
import pl.starter.android.utils.SessionRepositoryImpl

@Module
open class RepositoryProvidersModule {

    @Provides
    fun provideSessionRepository(sharedPreferences: SharedPreferences): SessionRepository =
        SessionRepositoryImpl(sharedPreferences)

}
