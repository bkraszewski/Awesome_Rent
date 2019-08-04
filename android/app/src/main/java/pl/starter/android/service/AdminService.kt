package pl.starter.android.service

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AdminService {

    @GET("api/users")
    fun listUsers(): Single<UsersResponse>

    @POST("api/users")
    fun createUser(@Body user: UserRequest):Single<User>
}
