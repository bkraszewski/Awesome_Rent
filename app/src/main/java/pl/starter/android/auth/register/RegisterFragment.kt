package pl.starter.android.auth.register

import android.os.Bundle
import android.view.View
import pl.starter.android.R
import pl.starter.android.auth.AuthNavListener
import pl.starter.android.base.BaseFragment
import pl.starter.android.databinding.FragmentRegisterBinding

class RegisterFragment : BaseFragment<RegisterView, RegisterViewModel, FragmentRegisterBinding>(R.layout.fragment_register), RegisterView {

    var listener: AuthNavListener? = null

    override fun navigateToLogin() {
        listener?.onLoginRequested()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(this, RegisterViewModel::class.java)
        viewModel.onAttach(this)
    }

    override fun onDestroyView() {
        listener = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }
}
