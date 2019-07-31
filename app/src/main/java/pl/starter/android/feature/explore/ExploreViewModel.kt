package pl.starter.android.feature.explore

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import io.reactivex.Observable
import io.reactivex.Single
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.starter.android.BR
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.feature.explore.list.ApartmentRowItem
import pl.starter.android.service.*
import pl.starter.android.utils.BaseSchedulers
import pl.starter.android.utils.StringProvider
import timber.log.Timber
import java.math.BigDecimal
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

    private lateinit var user: User

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
    val selectedStateIndex = ObservableInt(0)
    val apartmentStates = ObservableArrayList<String>().apply {
        addAll(ApartmentStateFilter.values().map { it.toString() })
    }

    val apartments = ObservableArrayList<ApartmentRowItem>()
    val itemBinding = ItemBinding.of<String>(BR.role, R.layout.item_dropdown)

    val canAddNew = ObservableBoolean(false)
    val showNoResults = ObservableBoolean(false)

    fun setup(view: ExploreView){
        user = userRepository.getUser()
        showStatusFilter.set(user.role != Role.USER)
        canAddNew.set(user.role != Role.USER)

        loadApartments(user, buildFilters())
        observeLocalChanges()
    }

    private fun observeLocalChanges() {
        apiRepository.observeApartmentChanges()
            .subscribe {
                loadApartments(user, buildFilters())
            }.disposeOnClear()
    }


    private fun getApartmentsSubscription(filters:Filters?): Single<List<Apartment>> {
        return if(filters == null){
            apiRepository.getApartments()
        }else{
            apiRepository.getApartments(filters)
        }
    }

    private fun loadApartments(user: User, filters: Filters?) {
        val priceFormat = stringProvider.getString(R.string.explore_apartment_price)
        val areaFormat = stringProvider.getString(R.string.explore_apartment_area)
        val roomsFormat = stringProvider.getString(R.string.explore_apartment_rooms)

        inProgress.set(true)
        getApartmentsSubscription(filters)
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
                inProgress.set(false)
                showNoResults.set(items.isEmpty())

                if (error != null) {
                    view?.showMessage(stringProvider.getString(R.string.common_general_error))
                    error.printStackTrace()
                    Timber.e(error)
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


    fun onApplyFilters(){
        view?.hideFilters()

        val filters = buildFilters()

        loadApartments(user, filters)
    }

    private fun buildFilters(): Filters {
        return if (user.role == Role.USER) {
            buildUserFilters()
        } else {
            buildAdminFilters()
        }
    }

    private fun buildAdminFilters(): Filters {
        return Filters(priceMin = BigDecimal.valueOf(minPrice.get().toLong()),
            priceMax = BigDecimal.valueOf(maxPrice.get().toLong()),
            areaMax = maxSize.get(),
            areaMin = minSize.get(),
            roomsMax = maxRooms.get(),
            roomsMin = minRooms.get(),
            stateFilter = ApartmentStateFilter.valueOf(apartmentStates[selectedStateIndex.get()])

        )
    }

    private fun buildUserFilters(): Filters {
        return Filters(priceMin = BigDecimal.valueOf(minPrice.get().toLong()),
            priceMax = BigDecimal.valueOf(maxPrice.get().toLong()),
            areaMax = maxSize.get(),
            areaMin = minSize.get(),
            roomsMax = maxRooms.get(),
            roomsMin = minRooms.get(),
            stateFilter = ApartmentStateFilter.AVAILABLE
        )
    }

    fun onCreateNewApartment(){
        view?.navigateToCreateApartment()
    }
}
