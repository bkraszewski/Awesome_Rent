package pl.starter.android.feature.auth.login

import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import pl.starter.android.feature.auth.register.RegisterView
import pl.starter.android.feature.auth.register.RegisterViewModel
import pl.starter.android.service.ApiRepository
import pl.starter.android.service.AuthReponse
import pl.starter.android.service.User
import pl.starter.android.utils.BaseSchedulers
import pl.starter.android.utils.StringProvider
import pl.starter.android.utils.TestSchedulerImpl
import org.hamcrest.CoreMatchers.`is` as Is

class RegisterViewModelTest {

    lateinit var cut: RegisterViewModel
    lateinit var stringProvider: StringProvider
    lateinit var baseSchedulers: BaseSchedulers
    lateinit var apiRepository: ApiRepository

    lateinit var registerView: RegisterView

    @Before
    fun setUp() {
        stringProvider = mock {
            on { getString(com.nhaarman.mockitokotlin2.any()) } doReturn "text"
        }
        baseSchedulers = TestSchedulerImpl()
        apiRepository = mock()
        registerView = mock()

        cut = RegisterViewModel(apiRepository, baseSchedulers, stringProvider)
        cut.onAttach(registerView)
    }

    @Test
    fun shouldShowLoginErrorWhenLoginIsEmpty() {
        cut.email.set("")
        cut.onRegister()

        assertThat(cut.emailError.get()?.isEmpty(), Is(equalTo(false)))
    }

    @Test
    fun shouldShowLoginErrorWhenLoginIsNotValid() {
        cut.email.set("bkraszewski")
        cut.onRegister()

        assertThat(cut.emailError.get()?.isEmpty(), Is(equalTo(false)))
    }

    @Test
    fun shouldShowPasswordErrorWhenPasswordIsEmpty() {
        cut.email.set("bkraszewski@gmail.com")
        cut.password.set("")
        cut.onRegister()

        assertThat(cut.passwordError.get()?.isEmpty(), Is(equalTo(false)))
    }

    @Test
    fun shouldShowPasswordErrorWhenRepeatPasswordIsEmpty() {
        cut.email.set("bkraszewski@gmail.com")
        cut.password.set("test1")
        cut.repeatPassword.set("")
        cut.onRegister()

        assertThat(cut.repeatPasswordError.get()?.isEmpty(), Is(equalTo(false)))
    }

    @Test
    fun shouldShowPasswordErrorWhenPasswordsAreDifferent() {
        cut.email.set("bkraszewski@gmail.com")
        cut.password.set("test1")
        cut.repeatPassword.set("test2")
        cut.onRegister()

        assertThat(cut.passwordError.get()?.isEmpty(), Is(equalTo(false)))
    }

    @Test
    fun shouldShowErrorMessageWhenFailedToRegister() {

        whenever(apiRepository.register(any(), any())).thenReturn(Single.error(Exception("random reason")))
        cut.email.set("bkraszewski@gmail.com")
        cut.password.set("pass")
        cut.repeatPassword.set("pass")
        cut.onRegister()

        verify(registerView).showMessage(anyString())
    }

    @Test
    fun shouldNavigateToMainScreenOnSuccessRegister() {

        whenever(apiRepository.register(any(), any())).thenReturn(Single.just(AuthReponse("", User("1",""))))
        cut.email.set("bkraszewski@gmail.com")
        cut.password.set("pass")
        cut.repeatPassword.set("pass")
        cut.onRegister()

        verify(registerView).navigateToMain()
    }
}
