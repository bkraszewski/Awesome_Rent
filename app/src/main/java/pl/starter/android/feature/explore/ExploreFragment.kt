package pl.starter.android.feature.explore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_explore.*
import pl.starter.android.R
import pl.starter.android.base.BaseFragment
import pl.starter.android.databinding.FragmentProfileBinding
import pl.starter.android.feature.explore.list.RentListFragment
import pl.starter.android.feature.explore.map.RentMapFragment


class ExploreFragment : BaseFragment<ExploreView, ExploreViewModel,
    FragmentProfileBinding>(R.layout.fragment_explore), ExploreView {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(this, ExploreViewModel::class.java)

        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(RentListFragment(), getString(R.string.explore_list))
        viewPagerAdapter.addFragment(RentMapFragment(), getString(R.string.explore_map))
        explorePager.adapter = viewPagerAdapter


        val listTab = tabLayout.newTab()
        val mapTab = tabLayout.newTab()

        listTab.text = viewPagerAdapter.getPageTitle(0)
        mapTab.text = viewPagerAdapter.getPageTitle(1)

        tabLayout.addTab(listTab, 0)
        tabLayout.addTab(mapTab, 1)

        tabLayout.setupWithViewPager(explorePager)
    }


    class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val fragmentTitleList = ArrayList<String>()
        private val fragmentList = ArrayList<Fragment>()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitleList[position]
        }

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }
    }
}
