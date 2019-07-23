package pl.starter.android.utils

import okhttp3.Interceptor
import okhttp3.Response

const val AUTHORIZATION_HEADER = "Authorization"

class TokenInterceptor(private val sessionsRepository: SessionRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val token = sessionsRepository.getToken()
        if (token.isNotEmpty()) {
            builder.addHeader(AUTHORIZATION_HEADER, "Token $token")
        }
        return chain.proceed(builder.build())
    }
}
