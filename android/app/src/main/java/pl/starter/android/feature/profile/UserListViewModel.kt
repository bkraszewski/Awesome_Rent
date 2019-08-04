package pl.starter.android.feature.profile

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.starter.android.BR
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.service.ApiRepository
import pl.starter.android.service.User
import pl.starter.android.service.UserRepository
import pl.starter.android.utils.BaseSchedulers
import timber.log.Timber
import javax.inject.Inject

interface UserListView : BaseView {
    fun showUser(user: User)
}

class UserListViewModel @Inject constructor(
    private val apiRepository: ApiRepository,
    private val userRepository: UserRepository,
    private val baseSchedulers: BaseSchedulers
) : BaseViewModel<UserListView>() {

    val users = ObservableArrayList<User>()
    val itemBinding = ItemBinding.of<User>(BR.user, R.layout.item_user)
        .bindExtra(BR.listener, object : UserRowListener {
            override fun onItemRequested(user: User) {
                view?.showUser(user)
            }

        })

    val showNoResults = ObservableBoolean(false)

    init {
        apiRepository.observeAdminChanges()
            .subscribe {
                requestUsers()
            }.disposeOnClear()
    }

    fun requestUsers() {
        inProgress.set(true)
        apiRepository.getUsers()
            .subscribeOn(baseSchedulers.io())
            .observeOn(baseSchedulers.main())
            .subscribe { it, error ->
                showNoResults.set(it.isEmpty())
                inProgress.set(false)

                if(error != null){
                    Timber.e(error)
                    view?.showMessage(R.string.common_general_error)
                    return@subscribe
                }


                users.clear()
                users.addAll(it)
            }.disposeOnDetach()
    }

    override fun onAttach(view: UserListView) {
        super.onAttach(view)
        apiRepository.observeUsersChanges().subscribe {
            requestUsers()
        }.disposeOnClear()
    }
}
