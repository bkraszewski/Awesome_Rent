package pl.starter.android.feature.edit_create_apartment

import androidx.databinding.ObservableInt
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.service.ApiRepository
import pl.starter.android.service.Role
import pl.starter.android.service.UserRepository
import javax.inject.Inject

interface EditCreateApartmentView : BaseView {
    
}

class EditCreateApartmentViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val apiRepository: ApiRepository
) : BaseViewModel<EditCreateApartmentView>() {



    override fun onAttach(view: EditCreateApartmentView) {
        super.onAttach(view)



    }
}
