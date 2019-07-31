package pl.starter.android.feature.explore

import pl.starter.android.feature.explore.list.ApartmentRowItem

interface HasApartmentData {
    fun setApartmentData(items: List<ApartmentRowItem>)
}
