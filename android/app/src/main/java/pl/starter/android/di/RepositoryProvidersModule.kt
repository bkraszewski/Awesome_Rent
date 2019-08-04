package pl.starter.android.di

import com.google.firebase.auth.FirebaseAuth
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
    fun provideSessionRepository(firebaseAuth: FirebaseAuth): SessionRepository =
        SessionRepositoryImpl(firebaseAuth)

    @Provides
    @Singleton
    fun provideApiRepository(apiService: ApiService,
                             userRepository: UserRepository,
                             firebaseAuth: FirebaseAuth,
                             sessionRepository: SessionRepository): ApiRepository = ApiRepositoryImpl(apiService, userRepository, firebaseAuth, sessionRepository)

    @Provides
    @Singleton
    fun provideUserRepository(firebaseAuth: FirebaseAuth): UserRepository {
        return UserRepositoryImpl(firebaseAuth)
    }

}
