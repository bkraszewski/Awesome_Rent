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
import pl.starter.android.feature.explore.ExploreFragment
import pl.starter.android.feature.profile.ProfileFragment
import pl.starter.android.feature.profile.UserListFragment

class MainActivity : BaseActivity<MainView, MainViewModel, ActivityMainBinding>(), MainView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup(R.layout.activity_main, this, MainViewModel::class.java)
    }

    override fun setupTabView(showUsers: Boolean) {
        setupFragments(showUsers)
    }

    private fun setupFragments(userTabVisible: Boolean) {
        bottomNavigation.menu.clear()
        bottomNavigation.inflateMenu(
            R.menu.menu_main
        )
        val fragmentList = mutableListOf<Fragment>()
        val menuItems = mutableListOf<Int>()

        fragmentList.add(ExploreFragment())
        menuItems.add(R.id.navigation_explore)

        if (userTabVisible) {
            fragmentList.add(UserListFragment.newInstance())
            menuItems.add(R.id.navigation_users)
        } else {
            bottomNavigation.menu.findItem(R.id.navigation_users).isVisible = false
        }

        fragmentList.add(ProfileFragment())
        menuItems.add(R.id.navigation_profile)


        mainViewPager.adapter =
            MainViewPager(fragmentList, supportFragmentManager)
        mainViewPager.offscreenPageLimit = 3

        binding.bottomNavigation.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                mainViewPager.currentItem = menuItems.indexOf(item.itemId)
                return true
            }

        })

        bottomNavigation.labelVisibilityMode = BottomNavigationView.VISIBLE
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
