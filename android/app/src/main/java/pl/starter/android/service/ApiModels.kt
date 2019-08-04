package pl.starter.android.service

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

//const val ROLE_USER = "USER"
//const val ROLE_REALTOR = "REALTOR"
//const val ROLE_ADMIN = "ADMIN"

enum class Role {
    USER, REALTOR, ADMIN
}

@Parcelize
data class User(val id: String, val email: String, val role: Role = Role.USER) :Parcelable

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val email: String, val password: String)

data class AuthReponse(val token: String, val user: User)

enum class ApartmentState {
    AVAILABLE, RENTED
}

enum class ApartmentStateFilter{
    ALL, AVAILABLE, RENTED
}

data class Filters(val priceMin: BigDecimal,
                   val priceMax: BigDecimal,
                   val areaMin: Int,
                   val areaMax:Int,
                   val roomsMin: Int,
                   val roomsMax : Int,
                   val stateFilter: ApartmentStateFilter
)

@Parcelize
data class Apartment(val id: String, val name: String, val description: String,
                     val floorAreaSize: BigDecimal, val pricePerMonth: BigDecimal, val rooms: Int,
                     val latitude: Double, val longitude: Double, val addedTimestamp: Long, val realtorId: String,
                     val realtorEmail: String, val state: ApartmentState) : Parcelable
