package pl.starter.android.feature.auth.register

import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.utils.StringProvider
import javax.inject.Inject

interface RegisterView : BaseView {
    fun navigateToLogin()
}

class RegisterViewModel @Inject constructor(
    private val stringProvider: StringProvider
) : BaseViewModel<RegisterView>() {


    override fun onAttach(view: RegisterView) {
        super.onAttach(view)

    }

    fun onLoginRequested(){
        view?.navigateToLogin()
    }
}
