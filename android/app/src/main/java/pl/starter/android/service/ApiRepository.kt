package pl.starter.android.service

import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import pl.starter.android.utils.SessionRepository

interface ApiRepository {
    fun login(email: String, password: String): Single<User>
    fun register(email: String, password: String): Single<User>
    fun logout()

    fun getApartments(): Single<List<Apartment>>
    fun getApartments(filters: Filters): Single<List<Apartment>>
    fun createApartment(apartment: Apartment): Single<Apartment>
    fun deleteApartment(apartment: Apartment): Completable
    fun editApartment(apartment: Apartment): Single<Apartment>
    fun observeApartmentChanges(): Observable<Any>

    fun getUsers(): Single<List<User>>
    fun editUser(userId: String, user: User): Single<User>
    fun deleteUser(userId: String): Completable
    fun createUser(user: User): Single<User>
    fun observeUsersChanges(): Observable<Any>
    fun observeAdminChanges():Observable<Any>
}

class ApiRepositoryImpl(
    private val apiService: ApiService,
    private val userRepository: UserRepository,
    private val adminService: AdminService) : ApiRepository {


    private val usersChangesSubject = PublishSubject.create<Any>()
    private val apartmentsChangesSubject = PublishSubject.create<Any>()
    private val adminChangesSubject = PublishSubject.create<Any>()


    override fun observeAdminChanges(): Observable<Any> {
        return adminChangesSubject
    }

    override fun getUsers(): Single<List<User>> {
        return adminService.listUsers().flatMap {
            Single.just(it.users)
        }
    }

    override fun editUser(userId: String, user: User): Single<User> {
        return adminService.editUser(userId, UserRequest(user.email, user.role.toString())).doOnSuccess {
            adminChangesSubject.onNext(1)
        }.flatMap { Single.just(user) }
    }

    override fun deleteUser(userId: String): Completable {
        return adminService.deleteUser(userId).doOnComplete {
            adminChangesSubject.onNext(1)
        }

    }

    override fun createUser(user: User): Single<User> {
        return adminService.createUser(UserRequest(user.email, user.role.toString())).doOnSuccess {
            adminChangesSubject.onNext(1)
        }
    }

    override fun editApartment(apartment: Apartment): Single<Apartment> {
        return apiService.editApartment(apartment.id, apartment)
            .doOnSuccess {
                apartmentsChangesSubject.onNext(1)
            }
    }

    override fun deleteApartment(apartment: Apartment): Completable {
        return apiService.deleteApartment(apartment.id)
            .doOnComplete {
                apartmentsChangesSubject.onNext(1)
            }
    }

    override fun createApartment(apartment: Apartment): Single<Apartment> {
        return apiService.createApartment(apartment).doOnSuccess {
            apartmentsChangesSubject.onNext(1)
        }
    }

    override fun register(email: String, password: String): Single<User> {
        return apiService.register(RegisterRequest(email, password))
    }

    override fun login(email: String, password: String): Single<User> {
        return apiService.login(LoginRequest(email, password)).doOnSuccess {
//            userRepository.update(it.user)
//            sessionRepository.saveToken(it.token)
        }
    }

    override fun logout() {
        userRepository.logout()
    }

    override fun getApartments(): Single<List<Apartment>> {
        return apiService.getApartments()
    }

    override fun getApartments(filters: Filters): Single<List<Apartment>> {
        return apiService.getApartments(filters)
    }

    override fun observeApartmentChanges(): Observable<Any> {
        return apartmentsChangesSubject
    }

    override fun observeUsersChanges(): Observable<Any> {
        return usersChangesSubject
    }

}
