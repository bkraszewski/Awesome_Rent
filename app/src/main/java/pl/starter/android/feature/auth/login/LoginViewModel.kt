package pl.starter.android.feature.auth.login

import androidx.databinding.ObservableField
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.service.ApiRepository
import pl.starter.android.utils.BaseSchedulers
import pl.starter.android.utils.StringProvider
import java.util.regex.Pattern.compile
import javax.inject.Inject

interface LoginView : BaseView {
    fun navigateToRegister()
    fun navigateToMain()
}

class LoginViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val baseSchedulers: BaseSchedulers,
    private val stringProvider: StringProvider
) : BaseViewModel<LoginView>() {

    val login: ObservableField<String> = ObservableField("")
    val password: ObservableField<String> = ObservableField("")

    val loginError: ObservableField<String> = ObservableField("")
    val passwordError: ObservableField<String> = ObservableField("")

    private val emailRegex = compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
    )

    override fun onAttach(view: LoginView) {
        super.onAttach(view)

    }

    fun onRegisterRequested(){
        view?.navigateToRegister()
    }

    fun onLogin(){
        loginError.set("")
        passwordError.set("")

        if(isStringBlank(login.get())){
            loginError.set(stringProvider.getString(R.string.login_not_valid))
            return
        }

        if(isEmailInvalid(login.get())){
            loginError.set(stringProvider.getString(R.string.login_not_valid))
            return
        }

        if(isStringBlank(password.get())){
            passwordError.set(stringProvider.getString(R.string.login_password_not_valid))
            return
        }

        performLogin()

    }

    private fun performLogin() {
        inProgress.set(true)

        apiRepository.login(login.get()!!, password.get()!!)
            .subscribeOn(baseSchedulers.io())
            .observeOn(baseSchedulers.main())
            .subscribe { response, error ->

                inProgress.set(false)

                if(error != null){
                    view?.showMessage(stringProvider.getString(R.string.login_error_server_side))
                    return@subscribe
                }

                view?.navigateToMain()

            }.disposeOnDetach()

    }

    private fun isEmailInvalid(email: String?): Boolean {
        return !emailRegex.matcher(email).matches()
    }

    private fun isStringBlank(email: String?): Boolean {
        return email?.isBlank() ?: true
    }
}
