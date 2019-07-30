package pl.starter.android.feature.edit_create_apartment

import androidx.databinding.*
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.starter.android.BR
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.service.Apartment
import pl.starter.android.service.ApartmentState
import pl.starter.android.service.ApiRepository
import pl.starter.android.service.UserRepository
import pl.starter.android.utils.BaseSchedulers
import pl.starter.android.utils.StringProvider
import timber.log.Timber
import java.math.BigDecimal
import javax.inject.Inject

interface EditCreateApartmentView : BaseView {
    fun finish()
}

class EditCreateApartmentViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stringProvider: StringProvider,
    private val apiRepository: ApiRepository,
    private val baseSchedulers: BaseSchedulers
) : BaseViewModel<EditCreateApartmentView>() {

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
    val apartmentRealtorId = ObservableLong(0)

    val apartmentStatusIndex = ObservableInt(0)
    val apartmentStatuses = ObservableArrayList<String>()

    val itemBinding = ItemBinding.of<String>(BR.role, R.layout.item_dropdown)

    val canChangeRealtor = ObservableBoolean(false)
    val canChangeStatus = ObservableBoolean(true)
    val canSaveChanges = ObservableBoolean(true)
    val canChangeLocation = ObservableBoolean(false)

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

    override fun onAttach(view: EditCreateApartmentView) {
        super.onAttach(view)
        apartmentStatuses.clear()
        apartmentStatuses.addAll(ApartmentState.values().map { it.toString() })
    }

    fun onNewApartment() {
        val user = userRepository.getUser()

        canChangeLocation.set(true)
        canChangeRealtor.set(true)
        canSaveChanges.set(true)
        canChangeStatus.set(true)

        apartmentRealtorEmail.set(user.email)
        apartmentRealtorId.set(user.id)

    }

    fun onSave() {
        if (isFormInvalid()) return

        createApartment()

    }

    private fun isFormInvalid(): Boolean {
        for (entry in fieldWithErrorMapping.entries) {
            entry.value.set("")
            if (entry.key.get().isNullOrEmpty()) {
                entry.value.set(stringProvider.getString(R.string.form_field_required))
                return true
            }
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
                    Timber.e(error)
                    error.printStackTrace()
                    view?.showMessage(R.string.common_general_error)
                    return@subscribe
                }

                view?.finish()

            }.disposeOnDetach()
    }

    private fun buildApartment(): Apartment {
        return Apartment(id = -1, name = apartmentName.get()!!, description = apartmentDescription.get()!!,
            floorAreaSize = BigDecimal(apartmentAreaSize.get()!!),
            realtorEmail = apartmentRealtorEmail.get()!!, realtorId = apartmentRealtorId.get(),
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

    }
}
