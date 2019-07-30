package pl.starter.android.feature.explore.map

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.feature.explore.list.ApartmentRowItem
import pl.starter.android.service.ApiRepository
import pl.starter.android.service.UserRepository
import pl.starter.android.utils.StringProvider
import javax.inject.Inject


interface RentMapView : BaseView {

}

class RentMapViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val stringProvider: StringProvider,
    private val apiRepository: ApiRepository
) : BaseViewModel<RentMapView>() {

    val mapElementsSubject = PublishSubject.create<List<ApartmentRowItem>>()
    val mapSubject = PublishSubject.create<GoogleMap>()


    override fun onAttach(view: RentMapView) {
        super.onAttach(view)

        Observable.combineLatest(mapElementsSubject, mapSubject, BiFunction<List<ApartmentRowItem>, GoogleMap, Pair<List<ApartmentRowItem>, GoogleMap>> { mapItems, map ->
            Pair(mapItems, map)
        }).subscribe {
            showPointsOnMap(it)

        }.disposeOnDetach()
    }

    private fun showPointsOnMap(it: Pair<List<ApartmentRowItem>, GoogleMap>) {
        it.second.clear()
        val builder = LatLngBounds.Builder()

        for (item in it.first) {
            val latLng = LatLng(item.apartment.latitude, item.apartment.longitude)
            val marker = MarkerOptions().position(latLng).title(String.format("%s %s", item.apartment.name, item.priceLabel))
            it.second.addMarker(marker)
            builder.include(latLng)
        }

        if (it.first.isNotEmpty()) {
            it.second.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0))
        }
    }


}
