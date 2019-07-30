package pl.starter.android.feature.profile

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Single
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is
import org.hamcrest.core.IsEqual
import org.junit.Before
import org.junit.Test
import pl.starter.android.service.ApiRepository
import pl.starter.android.service.User
import pl.starter.android.service.UserRepository
import pl.starter.android.utils.TestSchedulerImpl

class UserListViewModelTest{
    private lateinit var cut: UserListViewModel
    private lateinit var userRepository:UserRepository
    private lateinit var apiRepository: ApiRepository
    private val baseSchedulers =  TestSchedulerImpl()
    private  lateinit var view : UserListView

    @Before
    fun setUp() {
        userRepository = mock()
        apiRepository = mock()
        view = mock()
        cut = UserListViewModel(apiRepository,userRepository, baseSchedulers)
    }

    @Test
    fun shouldShowListOfUsers(){

        whenever(apiRepository.getUsers()).thenReturn(Single.just(listOf(User(1, "test@wp.pl"))))
        cut.requestUsers()

        assertThat(cut.users.size, Is(IsEqual.equalTo(1)))
        assertThat(cut.showNoResults.get(), Is(IsEqual.equalTo(false)))

    }

    @Test
    fun shouldShowEmptyList(){

        whenever(apiRepository.getUsers()).thenReturn(Single.just(listOf()))
        cut.requestUsers()

        assertThat(cut.users.size, Is(IsEqual.equalTo(0)))
        assertThat(cut.showNoResults.get(), Is(IsEqual.equalTo(true)))

    }
}
