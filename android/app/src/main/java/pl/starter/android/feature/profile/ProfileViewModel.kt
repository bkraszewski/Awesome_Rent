package pl.starter.android.feature.profile

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.starter.android.BR
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.feature.auth.isEmailInvalid
import pl.starter.android.feature.auth.isStringBlank
import pl.starter.android.service.ApiRepository
import pl.starter.android.service.Role
import pl.starter.android.service.User
import pl.starter.android.service.UserRepository
import pl.starter.android.utils.BaseSchedulers
import pl.starter.android.utils.StringProvider
import timber.log.Timber
import javax.inject.Inject

interface ProfileView : BaseView {
    fun finish()
    fun navigateToStartPage()
}

class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stringProvider: StringProvider,
    private val apiRepository: ApiRepository,
    private val baseSchedulers: BaseSchedulers
) : BaseViewModel<ProfileView>() {


    val email = ObservableField("")
    val emailError = ObservableField("")
    val selectedItem = ObservableInt()
    val roles = ObservableArrayList<String>()
    val itemBinding = ItemBinding.of<String>(BR.role, R.layout.item_dropdown)
    val canDelete = ObservableBoolean(false)
    val showLogout = ObservableBoolean(false)

    private lateinit var saveAction: SaveAction
    private lateinit var user: User

    init {
        setupRoles()
    }

    fun onCurrentUserEditRequested() {
        canDelete.set(false)
        showLogout.set(true)
        saveAction = CurrentUserSaveAction(userRepository)
        getValidUser()
    }

    fun onNewUserRequested() {
        canDelete.set(false)
        showLogout.set(false)
        saveAction = NewUserAction(this)
        getValidUser()
    }

    fun onEditUserRequested(user: User) {
        canDelete.set(true)
        showLogout.set(false)
        saveAction = EditUserAction(this, user)
        getValidUser()
    }

    private fun getValidUser() {
        user = saveAction.getUser()
        filUserData()
    }

    private fun filUserData() {
        email.set(user.email)
        selectedItem.set(roles.indexOf(user.role.toString()))
    }

    private fun setupRoles() {
        roles.clear()
        roles.addAll(Role.values().map { it.toString() })
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
        saveAction.onSave(profile)
        view?.showMessage(R.string.profile_changed)
    }

    fun onLogout() {
        apiRepository.logout()
        view?.finish()
        view?.navigateToStartPage()

    }

    private fun editUserOtherThanCurrent(user: User) {
        inProgress.set(true)
        apiRepository.editUser(user.id, user)
            .subscribeOn(baseSchedulers.io())
            .observeOn(baseSchedulers.main())
            .subscribe { it, error ->
                handleResult(it, error)
            }.disposeOnDetach()
    }

    private fun handleResult(user: User?, error: Throwable?) {
        inProgress.set(false)
        if (error != null) {
            view?.showMessage(R.string.common_general_error)
            error.printStackTrace()
            Timber.e(error)
            return
        }

        view?.finish()
    }

    private fun createNewUser(user: User) {
        inProgress.set(true)
        apiRepository.createUser(user)
            .subscribeOn(baseSchedulers.io())
            .observeOn(baseSchedulers.main())
            .subscribe { it, error ->
                handleResult(it, error)
            }.disposeOnDetach()
    }

    fun onDeleteUser() {
        inProgress.set(true)
        apiRepository.deleteUser(user.id)
            .subscribeOn(baseSchedulers.io())
            .observeOn(baseSchedulers.main())
            .subscribe {
                view?.finish()
            }.disposeOnDetach()
    }

    interface SaveAction {
        fun onSave(user: User)
        fun getUser(): User
    }

    class CurrentUserSaveAction(private val userRepository: UserRepository) : SaveAction {
        override fun onSave(user: User) {
            userRepository.update(user)
        }

        override fun getUser(): User {
            return userRepository.getUser()
        }
    }

    class NewUserAction(private val viewModel: ProfileViewModel) : SaveAction {
        override fun onSave(user: User) {
            viewModel.createNewUser(user)
        }

        override fun getUser(): User {
            return User(-1, "", Role.USER)
        }
    }

    class EditUserAction(private val viewModel: ProfileViewModel,
                         private val user: User) : SaveAction {
        override fun onSave(user: User) {
            viewModel.editUserOtherThanCurrent(user)
        }

        override fun getUser(): User {
            return user
        }

    }
}
