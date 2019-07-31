package pl.starter.android.feature.auth.register

import android.os.Bundle
import android.view.View
import pl.starter.android.R
import pl.starter.android.feature.auth.AuthNavListener
import pl.starter.android.base.BaseFragment
import pl.starter.android.databinding.FragmentRegisterBinding
import pl.starter.android.feature.main.MainActivity

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

    override fun navigateToMain() {
        MainActivity.start(requireContext())
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }
}
