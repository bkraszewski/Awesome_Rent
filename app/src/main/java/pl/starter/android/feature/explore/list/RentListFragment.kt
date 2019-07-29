package pl.starter.android.feature.explore.list

import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_rent_list.*
import pl.starter.android.R
import pl.starter.android.base.BaseFragment
import pl.starter.android.databinding.FragmentProfileBinding
import pl.starter.android.feature.explore.HasApartmentData
import pl.starter.android.utils.GridSpaceItemDecoration

class RentListFragment : BaseFragment<RentListView, RentListViewModel,
    FragmentProfileBinding>(R.layout.fragment_rent_list), RentListView, HasApartmentData {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(this, RentListViewModel::class.java)

        rvApartmentList.addItemDecoration(GridSpaceItemDecoration(16,8))
    }

    override fun setApartmentData(items: List<ApartmentRowItem>){
        viewModel.apartments.clear()
        viewModel.apartments.addAll(items)
    }


}
