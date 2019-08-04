package pl.starter.android.service

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

interface UserRepository {
    fun getUser(): User
    fun update(user: User): Completable
    fun observeUserChanges(): Observable<User>
    fun logout()
}


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth) : UserRepository {
    private val subject = PublishSubject.create<User>()

    override fun getUser(): User {

        return firebaseAuth.currentUser?.let {
            User(it.uid, it.email!!, Role.fromUser(it))
        } ?: throw Exception("User not authorized!")
    }

    @SuppressLint("ApplySharedPref")
    override fun update(user: User): Completable {

        return Completable.create { emitter ->
            val current = firebaseAuth.currentUser

            current?.let {

                it.updateProfile(UserProfileChangeRequest.Builder()
                    .setDisplayName(user.role.toString()).build()).addOnCompleteListener {
                    subject.onNext(user)
                    emitter.onComplete()
                }

            }
        }

    }

    @SuppressLint("ApplySharedPref")
    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun observeUserChanges(): Observable<User> = subject

}
