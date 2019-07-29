package pl.starter.android.feature.explore

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import io.reactivex.Observable
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.starter.android.BR
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.feature.auth.isEmailInvalid
import pl.starter.android.feature.auth.isStringBlank
import pl.starter.android.feature.explore.list.ApartmentRowItem
import pl.starter.android.feature.explore.list.RentListView
import pl.starter.android.service.ApiRepository
import pl.starter.android.service.Role
import pl.starter.android.service.User
import pl.starter.android.service.UserRepository
import pl.starter.android.utils.BaseSchedulers
import pl.starter.android.utils.StringProvider
import javax.inject.Inject

interface ExploreView : BaseView {
    fun showFilters()
    fun hideFilters()
    fun navigateToCreateApartment()

}

class ExploreViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stringProvider: StringProvider,
    private val apiRepository: ApiRepository,
    private val baseSchedulers: BaseSchedulers
) : BaseViewModel<ExploreView>() {


    val apartments = ObservableArrayList<ApartmentRowItem>()

    val minSize = ObservableInt(0)
    val maxSize = ObservableInt(400)
    val sizeFilterLabel = ObservableField("")

    val minPrice = ObservableInt(0)
    val maxPrice = ObservableInt(9999)
    val priceFilterLabel = ObservableField("")

    val minRooms = ObservableInt(1)
    val maxRooms = ObservableInt(10)
    val roomsFilterLabel = ObservableField("")

    val showStatusFilter = ObservableBoolean(true)
    val showAvailableFilter = ObservableBoolean(true)
    val showRentedFilter = ObservableBoolean(false)
    val canAddNew = ObservableBoolean(false)

    override fun onAttach(view: ExploreView) {
        super.onAttach(view)

        val user = userRepository.getUser()
        showStatusFilter.set(user.role != Role.USER)
        canAddNew.set(user.role != Role.USER)

        getApartments(user, view)
    }

    private fun getApartments(user: User, view: ExploreView) {
        val priceFormat = stringProvider.getString(R.string.explore_apartment_price)
        val areaFormat = stringProvider.getString(R.string.explore_apartment_area)
        val roomsFormat = stringProvider.getString(R.string.explore_apartment_rooms)

        apiRepository.getApartments()
            .subscribeOn(baseSchedulers.io())
            .observeOn(baseSchedulers.main())
            .flatMapObservable { Observable.fromIterable(it) }
            .map {
                ApartmentRowItem(it, priceLabel = String.format(priceFormat, it.pricePerMonth.toString()),
                    areaLabel = String.format(areaFormat, it.floorAreaSize.toString()),
                    roomsLabel = String.format(roomsFormat, it.rooms.toString()),
                    showStatus = user.role == Role.REALTOR || user.role == Role.ADMIN)
            }
            .toList()
            .subscribe { items, error ->
                if (error != null) {
                    view.showMessage(stringProvider.getString(R.string.common_general_error))
                    return@subscribe
                }
                apartments.clear()
                apartments.addAll(items)
            }.disposeOnDetach()
    }


    fun onFiltersRequested(){
        view?.showFilters()
    }

    fun onCloseFilters(){
        view?.hideFilters()
    }

    fun onSizeFiltersChanged(){
        sizeFilterLabel.set(String.format(stringProvider.getString(R.string.filter_size), minSize.get().toString(), maxSize.get().toString()))
    }

    fun onPriceFiltersChanged(){
        priceFilterLabel.set(String.format(stringProvider.getString(R.string.filter_price), minPrice.get().toString(), maxPrice.get().toString()))
    }

    fun onRoomsFilterChanged() {
        roomsFilterLabel.set(String.format(stringProvider.getString(R.string.filter_rooms), minRooms.get().toString(), maxRooms.get().toString()))
    }

    fun onToggleValue(value: ObservableBoolean){
        value.set(!value.get())
    }

    fun onApplyFilters(){
        view?.hideFilters()
        //todo update filter params
    }

    fun onCreateNewApartment(){
        view?.navigateToCreateApartment()
    }
}
