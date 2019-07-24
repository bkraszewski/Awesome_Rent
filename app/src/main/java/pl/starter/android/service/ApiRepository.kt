package pl.starter.android.service

import io.reactivex.Single

interface ApiRepository {
    fun login(email:String, password: String): Single<AuthReponse>
    fun register(email: String, password: String): Single<AuthReponse>
}

class ApiRepositoryImpl(
    private val apiService: ApiService) : ApiRepository{

    override fun register(email: String, password: String): Single<AuthReponse> {
        return apiService.register(RegisterRequest(email, password))
    }

    override fun login(email: String, password: String): Single<AuthReponse> {
        return apiService.login(LoginRequest(email, password))
    }

}
