package pl.starter.android.feature.explore

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import androidx.databinding.ObservableInt
import androidx.databinding.ObservableList
import androidx.databinding.ObservableList.OnListChangedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import io.apptik.widget.MultiSlider
import kotlinx.android.synthetic.main.fragment_explore.*
import kotlinx.android.synthetic.main.layout_filters.*
import pl.starter.android.R
import pl.starter.android.base.BaseFragment
import pl.starter.android.databinding.FragmentProfileBinding
import pl.starter.android.feature.edit_create_apartment.EditCreateApartmentActivity
import pl.starter.android.feature.explore.list.ApartmentRowItem
import pl.starter.android.feature.explore.list.RentListFragment
import pl.starter.android.feature.explore.map.RentMapFragment


class ExploreFragment : BaseFragment<ExploreView, ExploreViewModel,
    FragmentProfileBinding>(R.layout.fragment_explore), ExploreView {

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private val slideAnimDuration = 250L

    private val apartmentsChangedCallback = object : OnListChangedCallback<ObservableList<ApartmentRowItem>>() {
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
        sender?.let {
            viewPagerAdapter.notifyFragmentsApartmentsChanged(it)
        }

    }

    override fun showFilters() {
        val animate = buildSlideInAnimation()
        filtersPanel?.startAnimation(animate)

        android.os.Handler().post {
            apartmentStates.setSelection(viewModel.selectedStateIndex.get())
        }
    }

    private fun buildSlideInAnimation(): TranslateAnimation {
        val animate = TranslateAnimation(0f, 0f,
            filtersPanel.height.toFloat(), 0f)

        animate.duration = slideAnimDuration
        animate.fillAfter = true
        animate.fillBefore = true
        animate.isFillEnabled = true
        animate.setAnimationListener(object : EmptyAnimationListener() {

            override fun onAnimationStart(animation: Animation?) {
                filtersPanel.visibility = View.VISIBLE
            }

        })
        return animate
    }

    override fun hideFilters() {
        val animate = buildSlideOutAnimation()
        filtersPanel?.startAnimation(animate)
    }

    private fun buildSlideOutAnimation(): TranslateAnimation {
        val animate = TranslateAnimation(0f, 0f, 0f,
            filtersPanel.height.toFloat())
        animate.duration = slideAnimDuration
        animate.setAnimationListener(object : EmptyAnimationListener() {

            override fun onAnimationEnd(animation: Animation?) {
                filtersPanel.visibility = View.GONE
            }
        })
        return animate
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(this, ExploreViewModel::class.java)

        setupTabbedUi()
        viewModel.apartments.addOnListChangedCallback(apartmentsChangedCallback)
        setupFilterListeners()
        viewModel.onSizeFiltersChanged()
        viewModel.setup(this)
    }

    private fun setupFilterListeners() {
        setupRangeSlider(priceRangeSlider, viewModel.minPrice, viewModel.maxPrice) { viewModel.onPriceFiltersChanged() }
        setupRangeSlider(sizeRangeSlider, viewModel.minSize, viewModel.maxSize) { viewModel.onSizeFiltersChanged() }
        setupRangeSlider(roomsRangeSlider, viewModel.minRooms, viewModel.maxRooms) { viewModel.onRoomsFilterChanged() }
    }

    private fun setupRangeSlider(slider: MultiSlider, minValue: ObservableInt, maxValue: ObservableInt, onChangeAction: (() -> Unit)) {

        slider.min = minValue.get()
        slider.max = maxValue.get()
        slider.getThumb(0).value = minValue.get()
        slider.getThumb(1).value = maxValue.get()
        onChangeAction()

        slider.setOnThumbValueChangeListener { multiSlider, thumb, thumbIndex, value ->
            if (thumbIndex == 0) {
                minValue.set(value)
            } else {
                maxValue.set(value)
            }

            onChangeAction()
        }
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

    override fun navigateToCreateApartment() {
        EditCreateApartmentActivity.startForNew(requireContext())
    }

    override fun onDestroyView() {
        viewModel.apartments.removeOnListChangedCallback(apartmentsChangedCallback)
        super.onDestroyView()
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

    fun notifyFragmentsApartmentsChanged(items: List<ApartmentRowItem>) {
        fragmentList.filter { it is HasApartmentData }
            .map { it as HasApartmentData }
            .forEach { it.setApartmentData(items) }
    }
}

open class EmptyAnimationListener : Animation.AnimationListener {
    override fun onAnimationRepeat(animation: Animation?) {
    }

    override fun onAnimationEnd(animation: Animation?) {
    }

    override fun onAnimationStart(animation: Animation?) {
    }

}
