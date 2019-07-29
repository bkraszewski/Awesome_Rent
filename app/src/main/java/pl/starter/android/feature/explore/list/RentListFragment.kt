package pl.starter.android.feature.explore.list

import android.os.Bundle
import android.view.View
import pl.starter.android.R
import pl.starter.android.base.BaseFragment
import pl.starter.android.databinding.FragmentProfileBinding

class RentListFragment : BaseFragment<RentListView, RentListViewModel,
    FragmentProfileBinding>(R.layout.fragment_rent_list), RentListView {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(this, RentListViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()


    }
}
