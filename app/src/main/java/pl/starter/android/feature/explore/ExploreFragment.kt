package pl.starter.android.feature.explore

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ObservableList.OnListChangedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.layout_filters.*
import pl.starter.android.R
import pl.starter.android.base.BaseFragment
import pl.starter.android.databinding.FragmentProfileBinding
import pl.starter.android.feature.explore.list.ApartmentRowItem
import pl.starter.android.feature.explore.list.RentListFragment
import pl.starter.android.feature.explore.map.RentMapFragment


class ExploreFragment : BaseFragment<ExploreView, ExploreViewModel,
    FragmentProfileBinding>(R.layout.fragment_explore), ExploreView {

    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val apartmentsChangedCallback =  object: OnListChangedCallback<ObservableList<ApartmentRowItem>>(){
        override fun onChanged(sender: ObservableList<ApartmentRowItem>?) {
            updateFragments(sender)
        }

        override fun onItemRangeRemoved(sender: ObservableList<ApartmentRowItem>?, positionStart: Int, itemCount: Int) {
            updateFragments(sender)
        }

        override fun onItemRangeMoved(sender: ObservableList<ApartmentRowItem>?, fromPosition: Int, toPosition: Int, itemCount: Int) {
            updateFragments(sender)
        }

        override fun onItemRangeInserted(sender: ObservableList<ApartmentRowItem>?, positionStart: Int, itemCount: Int) {
            updateFragments(sender)
        }

        override fun onItemRangeChanged(sender: ObservableList<ApartmentRowItem>?, positionStart: Int, itemCount: Int) {
            updateFragments(sender)
        }

    }

    private fun updateFragments(sender: ObservableList<ApartmentRowItem>?) {
        sender?.let{
            viewPagerAdapter.notifyFragmentsApartmentsChanged(it)
        }

    }

    override fun showFilters() {

        val animate = TranslateAnimation(
            0f,
            0f,
            filtersPanel.height.toFloat(),
            0f)
        animate.duration = 250
        animate.fillAfter = true
        animate.fillBefore = true
        animate.isFillEnabled = true
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
                filtersPanel.visibility = View.VISIBLE
            }

        })
        filtersPanel?.startAnimation(animate)
    }

    override fun hideFilters() {
        val animate = TranslateAnimation(
            0f,
            0f,
            0f,
            filtersPanel.height.toFloat())
        animate.duration = 250
        animate.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                filtersPanel.visibility = View.GONE
            }

        })
        filtersPanel?.startAnimation(animate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(this, ExploreViewModel::class.java)

        setupTabbedUi()
        viewModel.apartments.addOnListChangedCallback(apartmentsChangedCallback)
    }

    override fun onDestroyView() {
        viewModel.apartments.removeOnListChangedCallback(apartmentsChangedCallback)
        super.onDestroyView()
    }

    private fun setupTabbedUi() {
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
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

    fun notifyFragmentsApartmentsChanged(items: List<ApartmentRowItem>){
        fragmentList.filter { it is HasApartmentData }
            .map { it as HasApartmentData }
            .forEach { it.setApartmentData(items) }
    }
}
