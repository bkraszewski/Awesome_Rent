package pl.starter.android.feature.auth.login

import android.os.Bundle
import android.view.View
import pl.starter.android.R
import pl.starter.android.feature.auth.AuthNavListener
import pl.starter.android.base.BaseFragment
import pl.starter.android.databinding.FragmentLoginBinding
import pl.starter.android.feature.main.MainActivity

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

    override fun navigateToMain() {
        MainActivity.start(requireActivity())
        requireActivity().finish()
    }

    companion object{
        fun newInstance() = LoginFragment()
    }
}
