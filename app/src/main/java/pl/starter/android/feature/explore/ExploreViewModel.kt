package pl.starter.android.feature.explore

import androidx.databinding.ObservableArrayList
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

}

class ExploreViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stringProvider: StringProvider,
    private val apiRepository: ApiRepository,
    private val baseSchedulers: BaseSchedulers
) : BaseViewModel<ExploreView>() {


    val apartments = ObservableArrayList<ApartmentRowItem>()

    override fun onAttach(view: ExploreView) {
        super.onAttach(view)

        val user = userRepository.getUser()

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
}
