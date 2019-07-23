package pl.starter.android.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import pl.starter.android.R
import pl.starter.android.auth.login.LoginFragment
import pl.starter.android.auth.register.RegisterFragment
import pl.starter.android.base.BaseActivity
import pl.starter.android.databinding.ActivityAuthBinding
import javax.inject.Inject

class AuthActivity : BaseActivity<AuthView, AuthViewModel, ActivityAuthBinding>(), AuthView, HasSupportFragmentInjector, AuthNavListener {


    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup(R.layout.activity_auth, this, AuthViewModel::class.java)
        onLoginRequested()
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    override fun onLoginRequested() {
        val loginFragment = LoginFragment.newInstance()
        loginFragment.listener = this
        showFragment(loginFragment)
    }

    override fun onRegisterRequested() {
        val registerFragment = RegisterFragment.newInstance()
        registerFragment.listener = this
        showFragment(registerFragment)
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, AuthActivity::class.java))
        }
    }
}
