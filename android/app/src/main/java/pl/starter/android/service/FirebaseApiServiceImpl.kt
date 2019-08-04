package pl.starter.android.service

import io.reactivex.Completable
import io.reactivex.Single

class FirebaseApiServiceImpl : ApiService{
    override fun login(request: LoginRequest): Single<AuthReponse> {
        return Single.never()
    }

    override fun register(request: RegisterRequest): Single<AuthReponse> {
        return Single.never()
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
