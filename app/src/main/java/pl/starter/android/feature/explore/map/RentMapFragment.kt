package pl.starter.android.feature.explore.map

import android.os.Bundle
import android.view.View
import pl.starter.android.R
import pl.starter.android.base.BaseFragment
import pl.starter.android.databinding.FragmentProfileBinding

class RentMapFragment : BaseFragment<RentMapView, RentMapViewModel,
    FragmentProfileBinding>(R.layout.fragment_rent_map), RentMapView {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(this, RentMapViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()


    }
}
