package pl.starter.android.splash

import android.os.Bundle
import pl.starter.android.R
import pl.starter.android.auth.AuthActivity
import pl.starter.android.base.BaseActivity
import pl.starter.android.databinding.ActivitySplashBinding
import pl.starter.android.main.MainActivity

class SplashActivity : BaseActivity<SplashView, SplashViewModel, ActivitySplashBinding>(), SplashView {
    override fun navigateToMain() {
        finish()
//        MainActivity.start(this)
    }

    override fun navigateToAuth() {
        AuthActivity.start(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup(R.layout.activity_splash, this, SplashViewModel::class.java)
    }
}
