package pl.starter.android.service

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter

class FirebaseApiServiceImpl(
    private val firebaseAuth: FirebaseAuth) : ApiService {

    override fun login(request: LoginRequest): Single<User> {
        return Single.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(request.email, request.password).addOnCompleteListener { task ->
                readUserData(task, emitter)
            }
        }
    }

    private fun readUserData(task: Task<AuthResult>, emitter: SingleEmitter<User>) {
        if (task.isSuccessful) {
            val user = task.result?.user
            if (user != null) {
                val role = Role.fromUser(user)

                emitter.onSuccess(User(user.uid, user.email!!, role))
            } else {
                emitter.onError(Exception("Created user is null!"))
            }
        } else {
            //todo handle password to weak in vm
            emitter.onError(Throwable(task.exception))
        }
    }

    override fun register(request: RegisterRequest): Single<User> {

        return Single.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(request.email, request.password).addOnCompleteListener { task ->
                readUserData(task, emitter)
            }
        }
    }

    override fun createApartment(apartment: Apartment): Single<Apartment> {
        return Single.never()
    }

    override fun deleteApartment(id: String): Completable {
        return Completable.never()
    }

    override fun editApartment(id: String, apartment: Apartment): Single<Apartment> {
        return Single.never()
    }

    override fun getApartments(): Single<List<Apartment>> {
        return Single.never()
    }

    override fun getApartments(filters: Filters): Single<List<Apartment>> {
        return Single.never()
    }

    override fun getUsers(): Single<List<User>> {
        return Single.never()
    }

    override fun editUser(userId: String, user: User): Single<User> {
        return Single.never()
    }

    override fun deleteUser(userId: String): Completable {
        return Completable.never()
    }

    override fun createUser(user: User): Single<User> {
        return Single.never()
    }

}
