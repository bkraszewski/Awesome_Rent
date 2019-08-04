package pl.starter.android.service

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface AdminService {

    @GET("api/users")
    fun listUsers(): Single<UsersResponse>

    @POST("api/users")
    fun createUser(@Body user: UserRequest): Single<User>

    @PUT("api/users/{userId}")
    fun editUser(@Path("userId") userId: String, @Body user: UserRequest): Single<Response<Void>>

    @DELETE("api/users/{userId}")
    fun deleteUser(@Path("userId") userId: String) :Completable

}
