package pl.starter.android.service

import android.annotation.SuppressLint
import android.content.SharedPreferences
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface UserRepository{
    fun getUser(): User
    fun update(user: User)
    fun observeUserChanges(): Observable<User>
    fun logout()
}


const val USER_ID = "user_id"
const val USER_EMAIL = "user_email"
const val USER_ROLE = "user_role"

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class UserRepositoryImpl (
    private val sharedPreferences: SharedPreferences) : UserRepository{


    private val subject = PublishSubject.create<User>()

    override fun getUser(): User {
        return User(sharedPreferences.getLong(USER_ID, -1),
            sharedPreferences.getString(USER_EMAIL, ""),
                Role.valueOf(sharedPreferences.getString(USER_ROLE, Role.USER.toString())))
//            Role.ADMIN)
    }

    @SuppressLint("ApplySharedPref")
    override fun update(user: User) {
        sharedPreferences.edit().apply{
            putLong(USER_ID, user.id)
            putString(USER_EMAIL, user.email)
            putString(USER_ROLE, user.role.toString()
            )
        }.commit()
        subject.onNext(user)
    }

    override fun logout() {
        sharedPreferences.edit().apply{
            remove(USER_ID)
            remove(USER_EMAIL)
            remove(USER_ROLE)
        }.apply()
    }

    override fun observeUserChanges(): Observable<User>  = subject

}
