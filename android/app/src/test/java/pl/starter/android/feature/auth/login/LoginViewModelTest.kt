package pl.starter.android.feature.auth.login

import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import pl.starter.android.service.ApiRepository
import pl.starter.android.service.User
import pl.starter.android.utils.BaseSchedulers
import pl.starter.android.utils.StringProvider
import pl.starter.android.utils.TestSchedulerImpl
import org.hamcrest.CoreMatchers.`is` as Is

class LoginViewModelTest {

    lateinit var cut: LoginViewModel
    lateinit var stringProvider: StringProvider
    lateinit var baseSchedulers: BaseSchedulers
    lateinit var apiRepository: ApiRepository

    lateinit var loginView: LoginView

    @Before
    fun setUp() {
        stringProvider = mock {
            on { getString(com.nhaarman.mockitokotlin2.any()) } doReturn "text"
        }
        baseSchedulers = TestSchedulerImpl()
        apiRepository = mock()
        loginView = mock()

        cut = LoginViewModel(apiRepository, baseSchedulers, stringProvider)
        cut.onAttach(loginView)
    }

    @Test
    fun shouldShowLoginErrorWhenLoginIsEmpty() {
        cut.login.set("")
        cut.onLogin()

        assertThat(cut.loginError.get()?.isEmpty(), Is(equalTo(false)))
    }

    @Test
    fun shouldShowLoginErrorWhenLoginIsNotValid() {
        cut.login.set("bkraszewski")
        cut.onLogin()

        assertThat(cut.loginError.get()?.isEmpty(), Is(equalTo(false)))
    }

    @Test
    fun shouldShowPasswordErrorWhenPasswordIsEmpty() {
        cut.login.set("bkraszewski@gmail.com")
        cut.password.set("")
        cut.onLogin()

        assertThat(cut.passwordError.get()?.isEmpty(), Is(equalTo(false)))
    }

    @Test
    fun shouldShowErrorMessageWhenFailedToLogIn() {

        whenever(apiRepository.login(any(), any())).thenReturn(Single.error(Exception("random reason")))
        cut.login.set("bkraszewski@gmail.com")
        cut.password.set("pass")
        cut.onLogin()

        verify(loginView).showMessage(anyString())
    }

    @Test
    fun shouldNavigateToMainScreenOnSuccessLogin() {

        whenever(apiRepository.login(any(), any())).thenReturn(Single.just(User("1","")))
        cut.login.set("bkraszewski@gmail.com")
        cut.password.set("pass")
        cut.onLogin()

        verify(loginView).navigateToMain()
    }
}
