package pl.starter.android.feature.auth.register

import androidx.databinding.ObservableField
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.feature.auth.EMAIL_REGEX
import pl.starter.android.feature.auth.isEmailInvalid
import pl.starter.android.feature.auth.isStringBlank
import pl.starter.android.service.ApiRepository
import pl.starter.android.utils.BaseSchedulers
import pl.starter.android.utils.StringProvider
import javax.inject.Inject

interface RegisterView : BaseView {
    fun navigateToLogin()
    fun navigateToMain()
}

class RegisterViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val baseSchedulers: BaseSchedulers,
    private val stringProvider: StringProvider
) : BaseViewModel<RegisterView>() {

    val email: ObservableField<String> = ObservableField("")
    val password: ObservableField<String> = ObservableField("")
    val repeatPassword: ObservableField<String> = ObservableField("")

    val emailError: ObservableField<String> = ObservableField("")
    val passwordError: ObservableField<String> = ObservableField("")
    val repeatPasswordError: ObservableField<String> = ObservableField("")

    private val emailRegex = EMAIL_REGEX

    override fun onAttach(view: RegisterView) {
        super.onAttach(view)

    }

    fun onLoginRequested(){
        view?.navigateToLogin()
    }

    fun onRegister() {
        emailError.set("")
        passwordError.set("")
        repeatPasswordError.set("")

        if(isStringBlank(email.get())){
            emailError.set(stringProvider.getString(R.string.register_email_not_valid))
            return
        }

        if(isEmailInvalid(email.get())){
            emailError.set(stringProvider.getString(R.string.register_email_not_valid))
            return
        }

        if(isStringBlank(password.get())){
            passwordError.set(stringProvider.getString(R.string.register_password_not_valid))
            return
        }

        if(isStringBlank(repeatPassword.get())){
            repeatPasswordError.set(stringProvider.getString(R.string.register_password_not_valid))
            return
        }

        val pass = password.get()
        val repeatPass = repeatPassword.get()

        if(pass?.equals(repeatPass) == false){
            passwordError.set(stringProvider.getString(R.string.register_passwords_are_not_same))
            return
        }

        performRegister()

    }

    private fun performRegister() {
        inProgress.set(true)

        apiRepository.register(email.get()!!, password.get()!!)
            .subscribeOn(baseSchedulers.io())
            .observeOn(baseSchedulers.main())
            .subscribe { response, error ->

                inProgress.set(false)

                if(error != null){
                    view?.showMessage(stringProvider.getString(R.string.register_error_server_side))
                    return@subscribe
                }

                view?.navigateToMain()

            }.disposeOnDetach()
    }
}
