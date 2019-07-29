package pl.starter.android.feature.explore.list

import pl.starter.android.service.Apartment

data class ApartmentRowItem(val apartment: Apartment,
                       val priceLabel: String, val areaLabel: String, val roomsLabel: String) {
}
