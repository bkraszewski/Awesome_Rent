package pl.starter.android.feature.auth

import pl.starter.android.base.BaseView

interface AuthNavListener : BaseView {
    fun onLoginRequested()
    fun onRegisterRequested()
}
