package pl.starter.android.service

import io.reactivex.Single
import pl.starter.android.utils.SessionRepository

interface ApiRepository {
    fun login(email:String, password: String): Single<AuthReponse>
    fun register(email: String, password: String): Single<AuthReponse>
}

class ApiRepositoryImpl(
    private val apiService: ApiService,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository) : ApiRepository{

    override fun register(email: String, password: String): Single<AuthReponse> {
        return apiService.register(RegisterRequest(email, password)).doOnSuccess {
            userRepository.update(it.user)
            sessionRepository.saveToken(it.token)
        }
    }

    override fun login(email: String, password: String): Single<AuthReponse> {
        return apiService.login(LoginRequest(email, password)).doOnSuccess {
            userRepository.update(it.user)
            sessionRepository.saveToken(it.token)
        }
    }

}
