package pl.starter.android.feature.explore.list

import androidx.databinding.ObservableArrayList
import io.reactivex.Observable
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.starter.android.BR
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.feature.explore.ApartmentRowListener
import pl.starter.android.service.*
import pl.starter.android.utils.BaseSchedulers
import pl.starter.android.utils.StringProvider
import javax.inject.Inject

interface RentListView : BaseView {
    fun showApartment(apartment: Apartment)

}

class RentListViewModel @Inject constructor(

) : BaseViewModel<RentListView>() {

    val apartments = ObservableArrayList<ApartmentRowItem>()
    val itemBinding = ItemBinding.of<ApartmentRowItem>(BR.apartment, R.layout.item_apartment_card)
        .bindExtra(BR.listener, object: ApartmentRowListener{
            override fun onItemRequested(apartment: Apartment) {
                view?.showApartment(apartment)
            }

        })




}
