package pl.starter.android.feature.profile

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import pl.starter.android.service.Role
import pl.starter.android.service.User
import pl.starter.android.service.UserRepository
import pl.starter.android.utils.StringProvider

class ProfileViewModelTest {


    private lateinit var cut: ProfileViewModel
    private val userRepository = mock<UserRepository>()
    private val stringProvider =  mock<StringProvider>()
    private val view =  mock<ProfileView>()

    @Before
    fun setUp() {
        cut = ProfileViewModel(userRepository, stringProvider)
    }

    @Test
    fun shouldSaveCorrectUserOnSave(){
        val oldUser = User(1, "Hello@world.com")
        val newUser = User(1, "Hello@test.com", Role.ADMIN)
        whenever(userRepository.getUser()).thenReturn(oldUser)
        cut.onAttach(view)

        cut.email.set(newUser.email)
        cut.selectedItem.set(2)

        cut.onSave()
        verify(userRepository).update(newUser)


    }
}
