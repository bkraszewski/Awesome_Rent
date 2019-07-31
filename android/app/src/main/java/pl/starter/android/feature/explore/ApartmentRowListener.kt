package pl.starter.android.feature.explore

import pl.starter.android.service.Apartment

interface ApartmentRowListener {
    fun onItemRequested(apartment: Apartment)
}
