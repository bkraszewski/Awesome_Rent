package pl.starter.android.service

import java.math.BigDecimal

//const val ROLE_USER = "USER"
//const val ROLE_REALTOR = "REALTOR"
//const val ROLE_ADMIN = "ADMIN"

enum class Role {
    USER, REALTOR, ADMIN
}

data class User(val id: Long, val email: String, val role: Role = Role.USER)

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val email: String, val password: String)

data class AuthReponse(val token: String, val user: User)

enum class ApartmentState {
    AVAILABLE, RENTED
}

data class Apartment(val id: Long, val name: String, val description: String,
                    val floorAreaSize: BigDecimal, val pricePerMont: BigDecimal, val rooms: Int,
                    val latitude: Double, val longitude: Double, val addedTimestamp: Long, val realtorId: Long,
                    val realtorEmail: String, val state: ApartmentState)
