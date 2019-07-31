package pl.starter.android.feature.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import pl.starter.android.R
import pl.starter.android.base.BaseFragment
import pl.starter.android.databinding.FragmentProfileBinding
import pl.starter.android.feature.splash.SplashActivity
import pl.starter.android.service.User

class ProfileFragment : BaseFragment<ProfileView, ProfileViewModel,
    FragmentProfileBinding>(R.layout.fragment_profile), ProfileView {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(this, ProfileViewModel::class.java)

        binding.deleteUserIcon.setOnClickListener {
            showConfirmationDialog()
        }

        if(arguments?.getBoolean(CREATE_USER) == true){
            viewModel.onNewUserRequested()
            return
        }

        val editedUser = arguments?.getParcelable<User?>(EDIT_USER)
        editedUser?.let{
            viewModel.onEditUserRequested(it)
            return
        }

        viewModel.onCurrentUserEditRequested()

    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.form_confirm_delete_title)
            .setMessage(R.string.form_confirm_delete_user_message)
            .setPositiveButton(R.string.common_yes) { dialog, which ->
                viewModel.onDeleteUser()
            }.setNegativeButton(R.string.common_no) { dialog, which -> dialog.dismiss() }
            .show()
    }

    override fun onResume() {
        super.onResume()
        android.os.Handler().post {
            binding.profileRoleSpinner.setSelection(viewModel.selectedItem.get())
        }
    }

    override fun navigateToStartPage() {
        SplashActivity.start(requireActivity())
    }

    override fun finish() {
        activity?.finish()
    }

    companion object {

        const val CREATE_USER = "create_user"
        const val EDIT_USER = "edit_user"

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
                    putParcelable(EDIT_USER, user)
                }
            }
        }
    }
}
