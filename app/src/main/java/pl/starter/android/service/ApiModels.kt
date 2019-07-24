package pl.starter.android.service


data class User(val id: Long, val email: String)

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val email: String, val password: String)

data class AuthReponse(val token: String, val user: User)
