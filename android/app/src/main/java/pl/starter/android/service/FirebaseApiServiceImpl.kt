package pl.starter.android.service

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import java.math.BigDecimal

class FirebaseApiServiceImpl(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore) : ApiService {

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
        return editApartment(apartment.id, apartment)
    }

    private fun apartmentToDoc(apartment: Apartment): Map<String, Any> {
        val doc = mapOf<String, Any>(
            "id" to apartment.id,
            "addedTimestamp" to apartment.addedTimestamp,
            "apartment_state" to apartment.state.toString(),
            "rooms" to apartment.rooms,
            "price" to apartment.pricePerMonth.toDouble(),
            "lng" to apartment.longitude,
            "lat" to apartment.latitude,
            "realtor_id" to apartment.realtorId,
            "realtor_email" to apartment.realtorEmail,
            "floor_area_size" to apartment.floorAreaSize.toDouble(),
            "description" to apartment.description,
            "name" to apartment.name
        )
        return doc
    }

    override fun getApartments(): Single<List<Apartment>> {
        return Single.create { emitter ->
            firestore.collection("apartments")
                .get()
                .addOnSuccessListener { task ->
                    val apartments = mutableListOf<Apartment>()
                    for (document in task.documents) {
                        val apartment = apartmentFromDoc(document)
                        apartments.add(apartment)

                    }

                    emitter.onSuccess(apartments)
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    private fun apartmentFromDoc(document: DocumentSnapshot): Apartment {
        return Apartment(id = document["id"] as String,
            addedTimestamp = document["addedTimestamp"] as Long,
            state = ApartmentState.valueOf(document["apartment_state"] as String),
            rooms = (document["rooms"] as Long).toInt(),
            pricePerMonth = BigDecimal((document["price"] as Number).toString()),
            longitude = document["lng"] as Double,
            latitude = document["lat"] as Double,
            realtorId = document["realtor_id"] as String,
            realtorEmail = document["realtor_email"] as String,
            floorAreaSize = BigDecimal((document["floor_area_size"] as Number).toString()),
            description = document["description"] as String,
            name = document["name"] as String
        )
    }

    override fun getApartments(filters: Filters): Single<List<Apartment>> {
        return Single.create { emitter ->
            val ref = firestore.collection("apartments")
            var query = ref.whereGreaterThanOrEqualTo("price", filters.priceMin.toDouble())
                .whereLessThanOrEqualTo("price", filters.priceMax.toDouble())

            if(filters.stateFilter != ApartmentStateFilter.ALL){
                query = query.whereEqualTo("apartment_state", filters.stateFilter.toString())
            }

                query.get()
                .addOnSuccessListener { task ->
                    val apartments = mutableListOf<Apartment>()
                    for (document in task.documents) {
                        val apartment = apartmentFromDoc(document)

                        //filtering on device because firestore allows to filter by just 1 field lol
                        if (apartment.rooms >= filters.roomsMin && apartment.rooms <= filters.roomsMax
                            && apartment.floorAreaSize >= BigDecimal.valueOf(filters.areaMin.toLong())
                            && apartment.floorAreaSize <= BigDecimal.valueOf(filters.areaMax.toLong())
                        ) {
                            apartments.add(apartment)
                        }
                    }

                    emitter.onSuccess(apartments)
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun deleteApartment(id: String): Completable {
        return Completable.create { emitter ->
            firestore.collection("apartments").document(id).delete().addOnCompleteListener {
                emitter.onComplete()
            }
        }
    }

    override fun editApartment(id: String, apartment: Apartment): Single<Apartment> {
        return Single.create { emitter ->
            val doc = apartmentToDoc(apartment)
            firestore.collection("apartments").document(id)
                .set(doc).addOnSuccessListener {
                    emitter.onSuccess(apartment)
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }


}
