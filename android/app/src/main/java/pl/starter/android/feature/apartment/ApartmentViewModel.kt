package pl.starter.android.feature.apartment

import androidx.databinding.*
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.starter.android.BR
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.service.*
import pl.starter.android.utils.BaseSchedulers
import pl.starter.android.utils.StringProvider
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

interface ApartmentView : BaseView {
    fun finish()
    fun showLocationPicker(lat: Double, lng: Double)
}

class ApartmentViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stringProvider: StringProvider,
    private val apiRepository: ApiRepository,
    private val baseSchedulers: BaseSchedulers,
    private val uuidGenerator: UuidGenerator
) : BaseViewModel<ApartmentView>() {

    private lateinit var user: User
    private lateinit var editedApartment: Apartment

    var addedTimestamp: Long = System.currentTimeMillis()
    val apartmentName = ObservableField("")
    val apartmentDescription = ObservableField("")
    val apartmentRoomsCount = ObservableField("")
    val apartmentAreaSize = ObservableField("")
    val apartmentMonthlyPrice = ObservableField("")

    val apartmentLat = ObservableField("")
    val apartmentLng = ObservableField("")

    val apartmentRealtorEmail = ObservableField("")
    val apartmentRealtorEmailError = ObservableField("")
    val apartmentRealtorId = ObservableField("")

    val apartmentStatusIndex = ObservableInt()
    val apartmentStatuses = ObservableArrayList<String>()

    val itemBinding = ItemBinding.of<String>(BR.role, R.layout.item_dropdown)

    val canChangeRealtor = ObservableBoolean(false)
    val canChangeStatus = ObservableBoolean(true)
    val canSaveChanges = ObservableBoolean(true)
    val canChangeLocation = ObservableBoolean(false)
    val canDeleteApartment = ObservableBoolean(false)
    val canEditFormFields = ObservableBoolean(false)

    val apartmentNameError = ObservableField("")
    val apartmentDescriptionError = ObservableField("")
    val apartmentRoomsCountError = ObservableField("")
    val apartmentAreaSizeError = ObservableField("")
    val apartmentMonthlyPriceError = ObservableField("")


    val apartmentLatError = ObservableField("")
    val apartmentLngError = ObservableField("")

    private val fieldWithErrorMapping = mapOf(
        apartmentName to apartmentNameError,
        apartmentDescription to apartmentDescriptionError,
        apartmentRoomsCount to apartmentRoomsCountError,
        apartmentAreaSize to apartmentAreaSizeError,
        apartmentLat to apartmentLatError,
        apartmentLng to apartmentLngError,
        apartmentMonthlyPrice to apartmentMonthlyPriceError

    )

    fun initialize() {
        user = userRepository.getUser()
        apartmentStatuses.clear()
        apartmentStatuses.addAll(ApartmentState.values().map { it.toString() })
    }

    fun onNewApartment() {
        canDeleteApartment.set(false)
        canChangeLocation.set(true)
        canChangeRealtor.set(true)
        canSaveChanges.set(true)
        canChangeStatus.set(true)
        canEditFormFields.set(true)

        apartmentRealtorEmail.set(user.email)
        apartmentRealtorId.set(user.id)

    }

    fun onSave() {
        if (isFormInvalid()) return

        if (::editedApartment.isInitialized) {
            editApartment()
        } else {
            createApartment()
        }
    }

    private fun editApartment() {
        val apartment = buildApartment().copy(id = editedApartment.id)

        inProgress.set(true)

        apiRepository.editApartment(apartment)
            .subscribeOn(baseSchedulers.io())
            .observeOn(baseSchedulers.main())
            .subscribe { response, error ->
                inProgress.set(false)
                if (error != null) {
                    handleError(error)
                    return@subscribe
                }

                view?.finish()

            }.disposeOnDetach()
    }

    private fun isFormInvalid(): Boolean {
        //validate required fields
        for (entry in fieldWithErrorMapping.entries) {
            entry.value.set("")
            if (entry.key.get().isNullOrEmpty()) {
                entry.value.set(stringProvider.getString(R.string.form_field_required))
                return true
            }
        }

        //validate latitude
        val lat = apartmentLat.get()?.toDouble()!!
        if(lat < -90.0 || lat > 90.0){
            apartmentLatError.set(stringProvider.getString(R.string.form_field_not_valid))
            return true
        }

        //validate longitude
        val lng = apartmentLng.get()?.toDouble()!!
        if(lng < -180.0 || lng > 180.0){
            apartmentLngError.set(stringProvider.getString(R.string.form_field_not_valid))
            return true
        }

        return false
    }

    private fun createApartment() {
        val apartment = buildApartment()
        inProgress.set(true)

        apiRepository.createApartment(apartment)
            .subscribeOn(baseSchedulers.io())
            .observeOn(baseSchedulers.main())
            .subscribe { response, error ->
                inProgress.set(false)
                if (error != null) {
                    handleError(error)
                    return@subscribe
                }

                view?.finish()

            }.disposeOnDetach()
    }

    private fun handleError(error: Throwable) {
        Timber.e(error)
        error.printStackTrace()
        view?.showMessage(R.string.common_general_error)
    }

    private fun buildApartment(): Apartment {
        return Apartment(id = uuidGenerator.generate(), name = apartmentName.get()!!, description = apartmentDescription.get()!!,
            floorAreaSize = BigDecimal(apartmentAreaSize.get()!!),
            realtorEmail = apartmentRealtorEmail.get()!!, realtorId = apartmentRealtorId.get()!!,
            latitude = apartmentLat.get()!!.toDouble(),
            longitude = apartmentLng.get()!!.toDouble(),
            pricePerMonth = BigDecimal(apartmentMonthlyPrice.get()!!),
            rooms = apartmentRoomsCount.get()!!.toInt(),
            state = ApartmentState.values()[apartmentStatusIndex.get()],
            addedTimestamp = addedTimestamp)
    }

    fun onClose() {
        view?.finish()
    }

    fun onLocationChange() {
        val lat = apartmentLat.get()?.toDoubleOrNull() ?: 0.0
        val lng = apartmentLng.get()?.toDoubleOrNull() ?:0.0
        view?.showLocationPicker(lat, lng)
    }

    fun onShowExistingApartment(apartment: Apartment) {
        editedApartment = apartment
        setupFieldsEditability()
        filApartmentFields(apartment)
    }

    private fun filApartmentFields(apartment: Apartment) {
        apartmentName.set(apartment.name)
        apartmentDescription.set(apartment.description)
        apartmentRoomsCount.set(apartment.rooms.toString())
        apartmentAreaSize.set(apartment.floorAreaSize.toString())
        apartmentLat.set(apartment.latitude.toString())
        apartmentLng.set(apartment.longitude.toString())
        apartmentMonthlyPrice.set(apartment.pricePerMonth.toString())
        apartmentRealtorEmail.set(apartment.realtorEmail)
        apartmentRealtorId.set(apartment.realtorId)
        apartmentStatusIndex.set(apartmentStatuses.indexOf(apartment.state.toString()))
        addedTimestamp = apartment.addedTimestamp
    }

    private fun setupFieldsEditability() {
        val userCanEdit = user.role == Role.REALTOR || user.role == Role.ADMIN
        canChangeRealtor.set(userCanEdit)
        canSaveChanges.set(userCanEdit)
        canChangeLocation.set(userCanEdit)
        canChangeStatus.set(userCanEdit)
        canDeleteApartment.set(userCanEdit)
        canEditFormFields.set(userCanEdit)
    }

    fun onDelete() {
        inProgress.set(true)
        apiRepository.deleteApartment(editedApartment)
            .subscribeOn(baseSchedulers.io())
            .observeOn(baseSchedulers.main())
            .subscribe {
                inProgress.set(false)
                view?.finish()
            }.disposeOnDetach()

    }
}
