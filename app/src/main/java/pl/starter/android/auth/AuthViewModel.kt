package pl.starter.android.auth

import androidx.databinding.ObservableField
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.utils.StringProvider
import javax.inject.Inject

interface AuthView : BaseView

class AuthViewModel @Inject constructor(
    private val stringProvider: StringProvider
) : BaseViewModel<AuthView>() {


    override fun onAttach(view: AuthView) {
        super.onAttach(view)

    }
}
