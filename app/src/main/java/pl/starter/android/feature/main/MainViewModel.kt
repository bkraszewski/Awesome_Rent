package pl.starter.android.feature.main

import androidx.databinding.ObservableInt
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.service.Role
import pl.starter.android.service.UserRepository
import javax.inject.Inject

interface MainView : BaseView {
    fun setupTabView(showUsers: Boolean)
}

class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel<MainView>() {

    val currentBottomMenuIndex = ObservableInt(0)

    override fun onAttach(view: MainView) {
        super.onAttach(view)

        val user = userRepository.getUser()
        view.setupTabView(user.role == Role.ADMIN)

        userRepository.observeUserChanges().subscribe {
            view.setupTabView(it.role == Role.ADMIN)
        }.disposeOnDetach()

    }
}
