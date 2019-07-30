package pl.starter.android.service

import io.reactivex.Completable
import io.reactivex.Single
import pl.starter.android.utils.BaseSchedulers
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

interface ApiService {
    fun login(request: LoginRequest): Single<AuthReponse>
    fun register(request: RegisterRequest): Single<AuthReponse>
    fun getApartments(): Single<List<Apartment>>
    fun createApartment(apartment: Apartment): Single<Apartment>
    fun deleteApartment(id: Long): Completable
    fun editApartment(id: Long, apartment: Apartment): Single<Apartment>

}

//TODO fake service
class ApiServiceImpl(private val baseSchedulers: BaseSchedulers) : ApiService {


    private var highestId = 8L


    private val fakeAparments = mutableListOf(
        Apartment(1, "Top Apartment", "Really awesome aparment", BigDecimal.valueOf(100),
            BigDecimal.valueOf(2000), 4, 48.532976, 14.610996, System.currentTimeMillis(),
            1, "bkraszewski@gmail.com", ApartmentState.AVAILABLE),
        Apartment(2, "Small apartment", "Tiny but funt", BigDecimal.valueOf(20),
            BigDecimal.valueOf(700), 1, 49.226566, 8.637046, System.currentTimeMillis(),
            1, "bkraszewski@gmail.com", ApartmentState.AVAILABLE),
        Apartment(3, "Huge Apartment", "That's what I call big", BigDecimal.valueOf(250),
            BigDecimal.valueOf(5000), 7, 51.469408, 14.610996, System.currentTimeMillis(),
            1, "bkraszewski@gmail.com", ApartmentState.AVAILABLE),

        Apartment(4, "Medium Flat", "Just like that", BigDecimal.valueOf(60),
            BigDecimal.valueOf(900), 2, 51.905307, 21.990580, System.currentTimeMillis(),
            1, "bkraszewski@gmail.com", ApartmentState.RENTED),
        Apartment(5, "Upper Apartment", "Quite comfy", BigDecimal.valueOf(75),
            BigDecimal.valueOf(700), 3, 52.357361, 19.930998, System.currentTimeMillis(),
            1, "bkraszewski@gmail.com", ApartmentState.AVAILABLE),

        Apartment(6, "Hello Apartment", "Really average apartment", BigDecimal.valueOf(45),
            BigDecimal.valueOf(750), 2, 48.532976, 14.610996, System.currentTimeMillis(),
            1, "bkraszewski@gmail.com", ApartmentState.AVAILABLE),
        Apartment(7, "Cheap apartment", "Do you really want to rent it?", BigDecimal.valueOf(24),

            BigDecimal.valueOf(200), 1, 51.267285, 21.232619, System.currentTimeMillis(),
            1, "bkraszewski@gmail.com", ApartmentState.AVAILABLE)


    )


    override fun getApartments(): Single<List<Apartment>> {
        return Single.just(fakeAparments.toList())
            .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

    override fun login(request: LoginRequest): Single<AuthReponse> {
        return Single.just(AuthReponse("token", User(1, "bkraszewski@gmail.com")))
            .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

    override fun register(request: RegisterRequest): Single<AuthReponse> {
        return Single.just(AuthReponse("token", User(1, "bkraszewski@gmail.com")))
            .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

    override fun createApartment(apartment: Apartment): Single<Apartment> {
        val backendItem = apartment.copy(id = ++highestId)
        fakeAparments.add(backendItem)
        return Single.just(backendItem)
            .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

    override fun deleteApartment(id: Long): Completable {

        return Completable.fromAction {
            fakeAparments.removeAll { it.id == id }
        }
    }

    override fun editApartment(id: Long, apartment: Apartment): Single<Apartment> {
        return Single.just(apartment)
            .doOnSuccess {
                fakeAparments.removeAll { it.id == apartment.id }
                fakeAparments.add(apartment)
            } .delay(2, TimeUnit.SECONDS, baseSchedulers.computation())
    }

}
