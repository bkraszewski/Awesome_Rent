package pl.starter.android.service

const val ROLE_USER = "USER"
const val ROLE_REALTOR = "REALTOR"
const val ROLE_ADMIN = "ADMIN"

data class User(val id: Long, val email: String, val role: String = ROLE_USER)

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val email: String, val password: String)

data class AuthReponse(val token: String, val user: User)
