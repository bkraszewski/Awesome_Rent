package pl.starter.android.feature.profile

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.starter.android.BR
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.feature.auth.isEmailInvalid
import pl.starter.android.feature.auth.isStringBlank
import pl.starter.android.service.Role
import pl.starter.android.service.User
import pl.starter.android.service.UserRepository
import pl.starter.android.utils.StringProvider
import javax.inject.Inject

interface ProfileView : BaseView {

}

class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stringProvider: StringProvider
) : BaseViewModel<ProfileView>() {


    private lateinit var user: User
    val email = ObservableField("")
    val emailError = ObservableField("")
    val selectedItem = ObservableInt()
    val roles = ObservableArrayList<String>()
    val itemBinding = ItemBinding.of<String>(BR.role, R.layout.item_dropdown)

    override fun onAttach(view: ProfileView) {
        super.onAttach(view)
        roles.clear()
        roles.addAll(Role.values().map { it.toString() })

        user = userRepository.getUser()
        email.set(user.email)
        selectedItem.set(roles.indexOf(user.role.toString()))
    }

    fun onSave() {

        if (isStringBlank(email.get())) {
            emailError.set(stringProvider.getString(R.string.login_not_valid))
            return
        }

        if (isEmailInvalid(email.get())) {
            emailError.set(stringProvider.getString(R.string.login_not_valid))
            return
        }

        val profile = user.copy(email = email.get()!!, role = Role.values()[selectedItem.get()])
        userRepository.update(profile)
        view?.showMessage(R.string.profile_changed)
    }
}
