package pl.starter.android.service

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject
import pl.starter.android.utils.SessionRepository

interface ApiRepository {
    fun login(email: String, password: String): Single<AuthReponse>
    fun register(email: String, password: String): Single<AuthReponse>
    fun logout()

    fun getApartments(): Single<List<Apartment>>
    fun getApartments(filters: Filters): Single<List<Apartment>>
    fun createApartment(apartment: Apartment): Single<Apartment>
    fun deleteApartment(apartment: Apartment): Completable
    fun editApartment(apartment: Apartment): Single<Apartment>
    fun observeApartmentChanges(): Observable<Any>

    fun getUsers(): Single<List<User>>
    fun editUser(userId: Long, user: User): Single<User>
    fun deleteUser(userId: Long): Completable
    fun createUser(user: User): Single<User>
    fun observeUsersChanges(): Observable<Any>
}

class ApiRepositoryImpl(
    private val apiService: ApiService,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository) : ApiRepository {

    private val usersChangesSubject = PublishSubject.create<Any>()
    private val apartmentsChangesSubject = PublishSubject.create<Any>()

    override fun logout() {
        sessionRepository.removeToken()
        userRepository.logout()
    }

    override fun getUsers(): Single<List<User>> {
        return apiService.getUsers()
    }

    override fun editUser(userId: Long, user: User): Single<User> {
        return apiService.editUser(userId, user).doOnSuccess {
            usersChangesSubject.onNext(1)
        }
    }

    override fun deleteUser(userId: Long): Completable {
        return apiService.deleteUser(userId).doOnComplete {
            usersChangesSubject.onNext(1)
        }
    }

    override fun createUser(user: User): Single<User> {
        return apiService.createUser(user)
            .doOnSuccess {
                usersChangesSubject.onNext(1)
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

    override fun register(email: String, password: String): Single<AuthReponse> {
        return apiService.register(RegisterRequest(email, password)).doOnSuccess {
            userRepository.update(it.user)
            sessionRepository.saveToken(it.token)
        }
    }

    override fun login(email: String, password: String): Single<AuthReponse> {
        return apiService.login(LoginRequest(email, password)).doOnSuccess {
            userRepository.update(it.user)
            sessionRepository.saveToken(it.token)
        }
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
