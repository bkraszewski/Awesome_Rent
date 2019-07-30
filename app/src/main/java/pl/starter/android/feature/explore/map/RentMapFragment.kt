package pl.starter.android.feature.explore.map

import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import pl.starter.android.R
import pl.starter.android.base.BaseFragment
import pl.starter.android.databinding.FragmentProfileBinding
import pl.starter.android.feature.edit_create_apartment.EditCreateApartmentActivity
import pl.starter.android.feature.explore.HasApartmentData
import pl.starter.android.feature.explore.list.ApartmentRowItem
import pl.starter.android.service.Apartment


class RentMapFragment : BaseFragment<RentMapView, RentMapViewModel,
    FragmentProfileBinding>(R.layout.fragment_rent_map), RentMapView, OnMapReadyCallback, HasApartmentData {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup(this, RentMapViewModel::class.java)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun setApartmentData(apartments: List<ApartmentRowItem>) {
        viewModel.mapElementsSubject.onNext(apartments)
    }

    override fun showApartment(apartment: Apartment) {
        EditCreateApartmentActivity.startForView(requireContext(), apartment)
    }


    override fun onMapReady(map: GoogleMap?) {
        map?.let {
            map.uiSettings.isCompassEnabled = true
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true
            viewModel.mapSubject.onNext(it)

            map.setOnInfoWindowClickListener {
                viewModel.onMarkedSelected(it)
                true
            }
        }

    }

}
