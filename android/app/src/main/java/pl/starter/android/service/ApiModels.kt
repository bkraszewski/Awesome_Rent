package pl.starter.android.service

import android.os.Parcelable
import android.text.TextUtils
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal


enum class Role {
    USER, REALTOR, ADMIN;

    companion object {
        fun fromUser(user: FirebaseUser): Role {
            val roleString = user.displayName
            return if (TextUtils.isEmpty(roleString)) {
                Role.USER
            } else {
                Role.valueOf(roleString!!)
            }
        }
    }
}


@Parcelize
data class User(val id: String, val email: String, val role: Role = Role.USER) : Parcelable

data class LoginRequest(val email: String, val password: String)
data class RegisterRequest(val email: String, val password: String)


enum class ApartmentState {
    AVAILABLE, RENTED
}

enum class ApartmentStateFilter {
    ALL, AVAILABLE, RENTED
}

data class Filters(val priceMin: BigDecimal,
                   val priceMax: BigDecimal,
                   val areaMin: Int,
                   val areaMax: Int,
                   val roomsMin: Int,
                   val roomsMax: Int,
                   val stateFilter: ApartmentStateFilter
)

@Parcelize
data class Apartment(val id: String, val name: String, val description: String,
                     val floorAreaSize: BigDecimal, val pricePerMonth: BigDecimal, val rooms: Int,
                     val latitude: Double, val longitude: Double, val addedTimestamp: Long, val realtorId: String,
                     val realtorEmail: String, val state: ApartmentState) : Parcelable
