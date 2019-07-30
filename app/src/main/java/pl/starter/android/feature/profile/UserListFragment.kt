package pl.starter.android.feature.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_user_list.*
import pl.starter.android.R
import pl.starter.android.base.BaseFragment
import pl.starter.android.databinding.FragmentUserListBinding
import pl.starter.android.service.User
import pl.starter.android.utils.GridSpaceItemDecoration

class UserListFragment : BaseFragment<UserListView, UserListViewModel, FragmentUserListBinding>(R.layout.fragment_user_list), UserListView {
    override fun showUser(user: User) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(this, UserListViewModel::class.java)
        rvUserList.addItemDecoration(GridSpaceItemDecoration(16, 8))
        viewModel.requestUsers()
    }

    companion object{
        fun newInstance(): Fragment {
            return UserListFragment()
        }
    }
}
