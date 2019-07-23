package pl.starter.android.auth

import pl.starter.android.base.BaseView

interface AuthNavListener : BaseView {
    fun onLoginRequested()
    fun onRegisterRequested()
}
