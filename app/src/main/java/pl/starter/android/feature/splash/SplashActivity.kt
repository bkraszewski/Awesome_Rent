package pl.starter.android.feature.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import pl.starter.android.R
import pl.starter.android.feature.auth.AuthActivity
import pl.starter.android.base.BaseActivity
import pl.starter.android.databinding.ActivitySplashBinding
import pl.starter.android.feature.main.MainActivity

class SplashActivity : BaseActivity<SplashView, SplashViewModel, ActivitySplashBinding>(), SplashView {
    override fun navigateToMain() {
        finish()
        MainActivity.start(this)
    }

    override fun navigateToAuth() {
        finish()
        AuthActivity.start(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup(R.layout.activity_splash, this, SplashViewModel::class.java)
    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, SplashActivity::class.java))
        }
    }
}
