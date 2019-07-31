package pl.starter.android.feature.splash

import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.utils.SessionRepository
import javax.inject.Inject

interface SplashView : BaseView {
    fun navigateToMain()
    fun navigateToAuth()
}

class SplashViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : BaseViewModel<SplashView>() {

    override fun onAttach(view: SplashView) {
        super.onAttach(view)

        val token = sessionRepository.getToken()

        if (token == null) {
            view.navigateToAuth()
        } else {
            view.navigateToMain()
        }
    }
}
