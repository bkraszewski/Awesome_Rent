package pl.starter.android.feature.explore.list

import pl.starter.android.service.Apartment
import pl.starter.android.service.ApartmentState

data class ApartmentRowItem(val apartment: Apartment,
                            val priceLabel: String, val areaLabel: String, val roomsLabel: String,
                            val showStatus: Boolean = false, val canRent:Boolean = apartment.state == ApartmentState.AVAILABLE) {
}
