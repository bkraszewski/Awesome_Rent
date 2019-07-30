package pl.starter.android.feature.main

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import pl.starter.android.service.Role
import pl.starter.android.service.User
import pl.starter.android.service.UserRepository

class MainViewModelTest{
    lateinit var cut: MainViewModel
    lateinit var view: MainView
    lateinit var userRepository: UserRepository

    @Before
    fun setup(){
        view = mock()
        userRepository = mock()
        cut = MainViewModel(userRepository)

        whenever(userRepository.observeUserChanges()).thenReturn(Observable.never())
    }

    @Test
    fun shouldNotShowRealtoAndAdminTabForRegularUser(){
        whenever(userRepository.getUser()).thenReturn(User(1, "", Role.USER))
        cut.onAttach(view)

        verify(view).setupTabView(false)
    }

    @Test
    fun shouldNotShowRealtoAndAdminTabForRealtor(){
        whenever(userRepository.getUser()).thenReturn(User(1, "", Role.REALTOR))
        cut.onAttach(view)

        verify(view).setupTabView(false)
    }

    @Test
    fun shouldShowAllTabsToAdmin(){
        whenever(userRepository.getUser()).thenReturn(User(1, "", Role.ADMIN))
        cut.onAttach(view)

        verify(view).setupTabView(true)
    }
}
