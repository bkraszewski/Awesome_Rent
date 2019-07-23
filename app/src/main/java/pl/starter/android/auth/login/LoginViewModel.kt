package pl.starter.android.auth.login

import androidx.databinding.ObservableField
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.utils.StringProvider
import javax.inject.Inject

interface LoginView : BaseView {
    fun navigateToRegister()
}

class LoginViewModel @Inject constructor(
    private val stringProvider: StringProvider
) : BaseViewModel<LoginView>() {


    override fun onAttach(view: LoginView) {
        super.onAttach(view)

    }

    fun onRegisterRequested(){
        view?.navigateToRegister()
    }
}
