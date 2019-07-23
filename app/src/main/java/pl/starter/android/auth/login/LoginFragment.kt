package pl.starter.android.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.starter.android.R
import pl.starter.android.auth.AuthNavListener
import pl.starter.android.base.BaseFragment
import pl.starter.android.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<LoginView, LoginViewModel, FragmentLoginBinding>(R.layout.fragment_login), LoginView {

    var listener: AuthNavListener? = null

    override fun navigateToRegister() {
        listener?.onRegisterRequested()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(this, LoginViewModel::class.java)
        viewModel.onAttach(this)
    }

    override fun onDestroyView() {
        listener = null
        super.onDestroyView()
    }

    companion object{
        fun newInstance() = LoginFragment()
    }
}
