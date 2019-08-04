package pl.starter.android.service

import io.reactivex.Completable
import io.reactivex.Single
import pl.starter.android.utils.BaseSchedulers
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

interface ApiService {
    fun login(request: LoginRequest): Single<User>
    fun register(request: RegisterRequest): Single<User>

    fun createApartment(apartment: Apartment): Single<Apartment>
    fun deleteApartment(id: String): Completable
    fun editApartment(id: String, apartment: Apartment): Single<Apartment>

    fun getApartments(): Single<List<Apartment>>
    fun getApartments(filters: Filters): Single<List<Apartment>>

    fun getUsers(): Single<List<User>>
    fun editUser(userId: String, user: User): Single<User>
    fun deleteUser(userId: String): Completable
    fun createUser(user: User): Single<User>

}

//TODO fake service
class ApiServiceImpl(private val baseSchedulers: BaseSchedulers) : ApiService {

    private var highestId = 8L

    private val fakeAparments = mutableListOf(
        Apartment("1", "Top Apartment", "Really awesome aparment", BigDecimal.valueOf(100),
            BigDecimal.valueOf(2000), 4, 48.532976, 14.610996, System.currentTimeMillis(),
            "1", "bkraszewski@gmail.com", ApartmentState.AVAILABLE),
        Apartment("2", "Small apartment", "Tiny but fun", BigDecimal.valueOf(20),
            BigDecimal.valueOf(700), 1, 49.226566, 8.637046, System.currentTimeMillis(),
            "1", "bkraszewski@gmail.com", ApartmentState.AVAILABLE),
        Apartment("3", "Huge Apartment", "That's what I call big", BigDecimal.valueOf(250),
            BigDecimal.valueOf(5000), 7, 51.469408, 14.610996, System.currentTimeMillis(),
            "1", "bkraszewski@gmail.com", ApartmentState.AVAILABLE),

        Apartment("4", "Medium Flat", "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", BigDecimal.valueOf(60),
            BigDecimal.valueOf(900), 2, 51.905307, 21.990580, System.currentTimeMillis(),
            "1", "bkraszewski@gmail.com", ApartmentState.RENTED),
        Apartment("5", "Upper Apartment", "Quite comfy", BigDecimal.valueOf(75),
            BigDecimal.valueOf(700), 3, 52.357361, 19.930998, System.currentTimeMillis(),
            "1", "bkraszewski@gmail.com", ApartmentState.AVAILABLE),

        Apartment("6", "Hello Apartment", "Really average apartment", BigDecimal.valueOf(45),
            BigDecimal.valueOf(750), 2, 48.532976, 14.610996, System.currentTimeMillis(),
            "1", "bkraszewski@gmail.com", ApartmentState.AVAILABLE),
        Apartment("7", "Cheap apartment", "Do you really want to rent it?", BigDecimal.valueOf(24),

            BigDecimal.valueOf(200), 1, 51.267285, 21.232619, System.currentTimeMillis(),
            "1", "bkraszewski@gmail.com", ApartmentState.AVAILABLE)
    )

    private val fakeUsers = mutableListOf(
        User("1", "user@gmail.com", Role.USER),
        User("2", "tester@gmail.com", Role.USER),
        User("3", "joedoe@gmail.com", Role.USER),
        User("4", "realtor@tophomes.com", Role.REALTOR),
        User("5", "uberadmin@tophomes.com", Role.ADMIN))

    override fun getApartments(): Single<List<Apartment>> {
        return Single.just(fakeAparments.toList())
            .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

    override fun login(request: LoginRequest): Single<User> {
        return Single.just( User("1", "bkraszewski@gmail.com"))
            .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

    override fun register(request: RegisterRequest): Single<User> {
        return Single.just(User("1", "bkraszewski@gmail.com"))
            .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

    override fun createApartment(apartment: Apartment): Single<Apartment> {
        val backendItem = apartment.copy(id = (++highestId).toString())
        fakeAparments.add(backendItem)
        return Single.just(backendItem)
            .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

    override fun deleteApartment(id: String): Completable {

        return Completable.fromAction {
            fakeAparments.removeAll { it.id == id }
        }
    }

    override fun editApartment(id: String, apartment: Apartment): Single<Apartment> {
        return Single.just(apartment)
            .doOnSuccess {
                fakeAparments.removeAll { it.id == apartment.id }
                fakeAparments.add(apartment)
            }.delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

    override fun getApartments(filters: Filters): Single<List<Apartment>> {
        val filtered = fakeAparments.filter {
            it.pricePerMonth >= filters.priceMin &&
                it.pricePerMonth <= filters.priceMax
        }.filter {
            it.floorAreaSize.toInt() >= filters.areaMin &&
                it.floorAreaSize.toInt() <= filters.areaMax
        }.filter {
            if (filters.stateFilter == ApartmentStateFilter.AVAILABLE) {
                return@filter it.state == ApartmentState.AVAILABLE
            } else {
                true
            }
        }.filter {
            if (filters.stateFilter == ApartmentStateFilter.RENTED) {
                return@filter it.state == ApartmentState.RENTED
            } else {
                true
            }
        }.filter {
            it.rooms >= filters.roomsMin && it.rooms <= filters.roomsMax
        }

        return Single.just(filtered)
            .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

    override fun getUsers(): Single<List<User>> {
        return Single.just(fakeUsers.toList())
            .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

    override fun editUser(userId: String, user: User): Single<User> {
        fakeUsers.removeAll { it.id == userId }
        fakeUsers.add(user)
        return Single.just(user)
            .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

    override fun deleteUser(userId: String): Completable {
        return Completable.fromAction{
            fakeUsers.removeAll { it.id == userId }
        }
    }

    override fun createUser(user: User): Single<User> {
        val newUser = user.copy(id = (++highestId).toString())
        fakeUsers.add(newUser)
        return Single.just(newUser)
            .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

}
