package pl.starter.android.feature.splash

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import pl.starter.android.utils.SessionRepository

class SplashViewModelTest {
    lateinit var cut: SplashViewModel
    lateinit var sessionRepository: SessionRepository
    lateinit var splashView: SplashView

    @Before
    fun setup() {
        sessionRepository = mock()
        splashView = mock()
        cut = SplashViewModel(sessionRepository)
    }

    @Test
    fun shouldShowAuthScreenWhenUserNotLogged() {
        doReturn(false).`when`(sessionRepository).isLoggedIn()

        cut.onAttach(splashView)
        verify(splashView).navigateToAuth()
    }

    @Test
    fun shouldShowMainScreenWhenUserIsLogged() {
        doReturn(true).`when`(sessionRepository).isLoggedIn()

        cut.onAttach(splashView)
        verify(splashView).navigateToMain()
    }
}
