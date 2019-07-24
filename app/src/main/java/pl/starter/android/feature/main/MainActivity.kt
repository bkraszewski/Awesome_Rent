package pl.starter.android.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import pl.starter.android.R
import pl.starter.android.base.BaseActivity
import pl.starter.android.databinding.ActivityMainBinding

class MainActivity : BaseActivity<MainView, MainViewModel, ActivityMainBinding>(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup(R.layout.activity_main, this, MainViewModel::class.java)
    }

    companion object{
        fun start(context: Context){
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }
}
