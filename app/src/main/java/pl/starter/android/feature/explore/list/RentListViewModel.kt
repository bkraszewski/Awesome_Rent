package pl.starter.android.feature.explore.list

import androidx.databinding.ObservableArrayList
import io.reactivex.Observable
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.starter.android.BR
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.service.ApiRepository
import pl.starter.android.service.Role
import pl.starter.android.service.User
import pl.starter.android.service.UserRepository
import pl.starter.android.utils.BaseSchedulers
import pl.starter.android.utils.StringProvider
import javax.inject.Inject

interface RentListView : BaseView {

}

class RentListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stringProvider: StringProvider,
    private val baseSchedulers: BaseSchedulers,
    private val apiRepository: ApiRepository
) : BaseViewModel<RentListView>() {

    val apartments = ObservableArrayList<ApartmentRowItem>()
    val itemBinding = ItemBinding.of<ApartmentRowItem>(BR.apartment, R.layout.item_apartment_card)


    override fun onAttach(view: RentListView) {
        super.onAttach(view)

        val user = userRepository.getUser()

        getApartments(user, view)

    }

    private fun getApartments(user: User, view: RentListView) {
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


}
