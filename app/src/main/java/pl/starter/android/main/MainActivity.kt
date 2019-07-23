package pl.starter.android.main

import android.os.Bundle
import pl.starter.android.R
import pl.starter.android.base.BaseActivity
import pl.starter.android.databinding.ActivityMainBinding

class MainActivity : BaseActivity<MainView, MainViewModel, ActivityMainBinding>(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup(R.layout.activity_main, this, MainViewModel::class.java)
    }
}
