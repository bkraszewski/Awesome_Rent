package pl.starter.android.feature.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import pl.starter.android.R
import pl.starter.android.base.BaseActivity
import pl.starter.android.databinding.ActivityMainBinding
import pl.starter.android.feature.auth.login.LoginFragment
import pl.starter.android.feature.auth.register.RegisterFragment
import pl.starter.android.feature.profile.ProfileFragment

class MainActivity : BaseActivity<MainView, MainViewModel, ActivityMainBinding>(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup(R.layout.activity_main, this, MainViewModel::class.java)
    }

    override fun setupTabView(realtorTabVisible: Boolean, adminTabVisible: Boolean) {
        setupBottomMenu()
        setupFragments(realtorTabVisible, adminTabVisible)

    }

    private fun setupFragments(realtorTabVisible: Boolean, adminTabVisible: Boolean) {
        bottomNavigation.menu.clear()
        bottomNavigation.inflateMenu(
            R.menu.menu_main
        )
        val fragmentList = mutableListOf<Fragment>()
        val menuItems = mutableListOf<Int>()

        fragmentList.add(LoginFragment())
        menuItems.add(R.id.navigation_explore)

        if (realtorTabVisible) {
            fragmentList.add(LoginFragment())
            menuItems.add(R.id.navigation_realtors)
        } else {
            bottomNavigation.menu.findItem(R.id.navigation_realtors).isVisible = false
        }

        if (adminTabVisible) {
            fragmentList.add(LoginFragment())
            menuItems.add(R.id.navigation_clients)
        } else {
            bottomNavigation.menu.findItem(R.id.navigation_clients).isVisible = false
        }

        fragmentList.add(ProfileFragment())
        menuItems.add(R.id.navigation_profile)


        mainViewPager.adapter =
            MainViewPager(fragmentList, supportFragmentManager)

        binding.bottomNavigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                mainViewPager.currentItem = menuItems.indexOf(item.itemId)
                return true
            }

        })

        bottomNavigation.labelVisibilityMode = BottomNavigationView.VISIBLE
    }

    private fun setupBottomMenu() {

    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    class MainViewPager(private val fragments: List<Fragment>,
                        fragmentManager: FragmentManager
    ) : FragmentStatePagerAdapter(fragmentManager) {
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

    }
}
