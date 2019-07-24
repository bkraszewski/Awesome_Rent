package pl.starter.android.feature.auth.login

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import io.reactivex.schedulers.TestScheduler
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import pl.starter.android.service.ApiRepository
import pl.starter.android.utils.BaseSchedulers
import pl.starter.android.utils.StringProvider
import pl.starter.android.utils.TestSchedulerImpl
import org.hamcrest.CoreMatchers.`is` as Is

class LoginViewModelTest {

    lateinit var cut: LoginViewModel
    lateinit var stringProvider: StringProvider
    lateinit var baseSchedulers: BaseSchedulers
    lateinit var apiRepository: ApiRepository

    @Before
    fun setUp() {
        stringProvider = mock {
            on { getString(com.nhaarman.mockitokotlin2.any()) } doReturn "text"
        }
        baseSchedulers = TestSchedulerImpl()
        apiRepository = mock()

        cut = LoginViewModel(apiRepository, baseSchedulers, stringProvider)
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

//    @Test
//    fun shouldShowProgressWhenLogging() {
//        cut.login.set("bkraszewski@gmail.com")
//        cut.password.set("pass")
//        cut.onLogin()
//
//        assertThat(cut.inProgress.get(), Is(equalTo(true)))
//    }
}
