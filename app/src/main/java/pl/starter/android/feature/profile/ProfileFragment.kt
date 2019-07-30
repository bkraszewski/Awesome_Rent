package pl.starter.android.feature.profile

import android.os.Bundle
import android.view.View
import pl.starter.android.R
import pl.starter.android.base.BaseFragment
import pl.starter.android.databinding.FragmentProfileBinding
import pl.starter.android.service.User

class ProfileFragment : BaseFragment<ProfileView, ProfileViewModel,
    FragmentProfileBinding>(R.layout.fragment_profile), ProfileView {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(this, ProfileViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        android.os.Handler().post {
            binding.profileRoleSpinner.setSelection(viewModel.selectedItem.get())
        }

    }

    companion object {

        const val CREATE_USER = "create_user"
        const val USER = "edit_user"

        fun newInstanceForNewUser(): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(CREATE_USER, true)
                }
            }
        }

        fun newInstanceForEditUser(user: User): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(USER, user)
                }
            }
        }
    }
}
