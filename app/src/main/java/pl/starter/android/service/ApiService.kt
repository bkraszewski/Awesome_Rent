package pl.starter.android.service

import io.reactivex.Single
import pl.starter.android.utils.BaseSchedulers
import java.util.concurrent.TimeUnit

interface ApiService {
    fun login(request: LoginRequest) : Single<AuthReponse>
    fun register(request: RegisterRequest):Single<AuthReponse>

}

//TODO fake service
class ApiServiceImpl(private val baseSchedulers: BaseSchedulers) : ApiService {
    override fun login(request: LoginRequest): Single<AuthReponse> {
        return Single.just(AuthReponse("token", User(1, "bkraszewski@gmail.com")))
            .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

    override fun register(request: RegisterRequest): Single<AuthReponse> {
            return Single.just(AuthReponse("token", User(1, "bkraszewski@gmail.com")))
                .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

}
