package pl.starter.android.feature.explore

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.starter.android.BR
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.feature.auth.isEmailInvalid
import pl.starter.android.feature.auth.isStringBlank
import pl.starter.android.service.ApiRepository
import pl.starter.android.service.Role
import pl.starter.android.service.User
import pl.starter.android.service.UserRepository
import pl.starter.android.utils.StringProvider
import javax.inject.Inject

interface ExploreView : BaseView {
    fun showFilters()
    fun hideFilters()

}

class ExploreViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stringProvider: StringProvider,
    private val apiRepository: ApiRepository
) : BaseViewModel<ExploreView>() {


    override fun onAttach(view: ExploreView) {
        super.onAttach(view)
    }

    fun onFiltersRequested(){
        view?.showFilters()
    }

    fun onCloseFilters(){
        view?.hideFilters()
    }
}
