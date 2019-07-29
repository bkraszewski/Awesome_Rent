package pl.starter.android.feature.explore.list

import androidx.databinding.ObservableArrayList
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.starter.android.BR
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.service.Apartment
import pl.starter.android.service.ApiRepository
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

    val apartments = ObservableArrayList<Apartment>()
    val itemBinding = ItemBinding.of<Apartment>(BR.apartment, R.layout.item_apartment_card)


    override fun onAttach(view: RentListView) {
        super.onAttach(view)

        apiRepository.getApartments()
            .subscribeOn(baseSchedulers.io())
            .observeOn(baseSchedulers.main())
            .subscribe { items, error ->
                if (error != null) {
                    view.showMessage(stringProvider.getString(R.string.common_general_error))
                    return@subscribe
                }

                apartments.addAll(items)
            }.disposeOnDetach()

    }


}
