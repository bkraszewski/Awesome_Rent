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




}
