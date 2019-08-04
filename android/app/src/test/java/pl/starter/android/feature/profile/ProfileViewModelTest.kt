package pl.starter.android.feature.profile

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Single
import org.hamcrest.core.IsEqual
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.ArgumentMatchers.anyString
import pl.starter.android.service.ApiRepository
import pl.starter.android.service.Role
import pl.starter.android.service.User
import pl.starter.android.service.UserRepository
import pl.starter.android.utils.StringProvider
import pl.starter.android.utils.TestSchedulerImpl

class ProfileViewModelTest {

    private lateinit var cut: ProfileViewModel
    private lateinit var userRepository: UserRepository
    private lateinit var stringProvider: StringProvider
    private lateinit var apiRepository: ApiRepository
    private val baseSchedulers = TestSchedulerImpl()
    private val view =  mock<ProfileView>()

    @Before
    fun setUp() {
        userRepository = mock()
        apiRepository = mock()
        stringProvider = mock()
        cut = ProfileViewModel(userRepository, stringProvider, apiRepository, baseSchedulers)
    }

    @Test
    fun shouldFilCurrentUserData(){
        val oldUser = User("1", "Hello@world.com", Role.REALTOR)
        whenever(userRepository.getUser()).thenReturn(oldUser)

        cut.onAttach(view)
        cut.onCurrentUserEditRequested()

        assertThat(cut.email.get()!!, IsEqual("Hello@world.com"))
        assertThat(cut.selectedItem.get(), IsEqual(1))
        assertThat(cut.canDelete.get(), IsEqual(false))
        assertThat(cut.showLogout.get(), IsEqual(true))
    }

    @Test
    fun shouldFilNewUserData(){
        val oldUser = User("1", "Hello@world.com", Role.REALTOR)
        whenever(userRepository.getUser()).thenReturn(oldUser)

        cut.onAttach(view)
        cut.onNewUserRequested()

        assertThat(cut.email.get()!!, IsEqual(""))
        assertThat(cut.selectedItem.get(), IsEqual(0))
        assertThat(cut.canDelete.get(), IsEqual(false))
        assertThat(cut.showLogout.get(), IsEqual(false))
    }

    @Test
    fun shouldFilEditedUserData(){
        val oldUser = User("1", "Hello@world.com", Role.REALTOR)
        whenever(userRepository.getUser()).thenReturn(oldUser)

        cut.onAttach(view)
        cut.onEditUserRequested(User("2", "john@doe.com", Role.ADMIN))

        assertThat(cut.email.get()!!, IsEqual("john@doe.com"))
        assertThat(cut.selectedItem.get(), IsEqual(2))
        assertThat(cut.canDelete.get(), IsEqual(true))
        assertThat(cut.showLogout.get(), IsEqual(false))
    }

    @Test
    fun shouldSaveCorrectUserOnSave(){
        val oldUser = User("1", "Hello@world.com")
        val newUser = User("1", "Hello@test.com", Role.ADMIN)
        whenever(userRepository.getUser()).thenReturn(oldUser)
        whenever(userRepository.update(any())).thenReturn(Completable.complete())
        cut.onAttach(view)
        cut.onCurrentUserEditRequested()

        cut.email.set(newUser.email)
        cut.selectedItem.set(2)

        cut.onSave()
        verify(userRepository).update(newUser)
    }

    @Test
    fun shouldSaveCorrectUserOnSaveWhenEditing(){
        val oldUser = User("1", "Hello@world.com")
        val newUser = User("2", "Hello@test.com", Role.ADMIN)
        whenever(userRepository.getUser()).thenReturn(oldUser)
        whenever(apiRepository.editUser(anyString(),any())).thenReturn(Single.just(newUser))
        cut.onAttach(view)
        cut.onEditUserRequested(newUser)

        cut.onSave()
        verify(apiRepository).editUser(newUser.id, newUser)
    }

    @Test
    fun shouldSaveCorrectUserOnSaveWhenNewUser(){
        val oldUser = User("1", "Hello@world.com")
        whenever(userRepository.getUser()).thenReturn(oldUser)

        whenever(apiRepository.createUser(any())).thenReturn(Single.just(oldUser))
        cut.onAttach(view)
        cut.onNewUserRequested()

        cut.email.set("lol@lol.pl")
        cut.selectedItem.set(1)

        cut.onSave()
        verify(apiRepository).createUser(User("", "lol@lol.pl", Role.REALTOR))
    }
}
