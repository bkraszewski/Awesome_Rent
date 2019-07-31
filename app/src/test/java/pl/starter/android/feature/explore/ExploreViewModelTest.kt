package pl.starter.android.feature.explore

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.Single
import org.hamcrest.core.Is
import org.hamcrest.core.IsEqual.equalTo
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt
import pl.starter.android.service.*
import pl.starter.android.utils.BaseSchedulers
import pl.starter.android.utils.StringProvider
import pl.starter.android.utils.TestSchedulerImpl
import java.math.BigDecimal

class ExploreViewModelTest {

    lateinit var cut: ExploreViewModel
    lateinit var userRepository: UserRepository
    lateinit var apiRepository: ApiRepository
    lateinit var baseSchedulers: BaseSchedulers
    lateinit var stringProvider: StringProvider
    lateinit var view: ExploreView

    @Before
    fun setUp() {
        userRepository = mock()
        apiRepository = mock()
        baseSchedulers = TestSchedulerImpl()
        stringProvider = mock()
        view = mock()


        cut = ExploreViewModel(userRepository, stringProvider, apiRepository, baseSchedulers)
    }

    @Test
    fun shouldLoadInitialData() {
        val apartment = listOf(Apartment(1, "Top Apartment", "Really awesome aparment", BigDecimal.valueOf(100),
            BigDecimal.valueOf(2000), 4, 48.532976, 14.610996, System.currentTimeMillis(),
            1, "bkraszewski@gmail.com", ApartmentState.AVAILABLE))

        whenever(apiRepository.getApartments(any())).thenReturn(Single.just(apartment))
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com"))
        whenever(stringProvider.getString(anyInt())).thenReturn("%s")

        cut.setup(view)

        assertThat(cut.apartments.size, Is(equalTo(1)))
        assertThat(cut.showNoResults.get(), Is(equalTo(false)))
    }

    @Test
    fun shouldShowNoResultsWhenNoData() {
        val apartment = listOf<Apartment>()

        whenever(apiRepository.getApartments(any())).thenReturn(Single.just(apartment))
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com"))
        whenever(stringProvider.getString(anyInt())).thenReturn("%s")

        cut.setup(view)

        assertThat(cut.apartments.size, Is(equalTo(0)))
        assertThat(cut.showNoResults.get(), Is(equalTo(true)))
    }

    @Test
    fun shouldApplyFiltersForUser() {
        val apartment = listOf(Apartment(1, "Top Apartment", "Really awesome aparment", BigDecimal.valueOf(100),
            BigDecimal.valueOf(2000), 4, 48.532976, 14.610996, System.currentTimeMillis(),
            1, "bkraszewski@gmail.com", ApartmentState.AVAILABLE))

        whenever(apiRepository.getApartments()).thenReturn(Single.just(apartment))
        whenever(apiRepository.getApartments(any())).thenReturn(Single.just(apartment))
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com"))
        whenever(stringProvider.getString(anyInt())).thenReturn("%s")
        whenever(apiRepository.observeApartmentChanges()).thenReturn(Observable.never())

        cut.setup(view)
        cut.onAttach(view)

        cut.minSize.set(100)
        cut.maxSize.set(200)
        cut.minRooms.set(1)
        cut.maxRooms.set(2)
        cut.minPrice.set(999)
        cut.maxPrice.set(1001)
        cut.onApplyFilters()

        verify(apiRepository).getApartments(Filters(priceMin = BigDecimal.valueOf(999),
            priceMax = BigDecimal.valueOf(1001),
            areaMax = 200,
            areaMin = 100,
            roomsMax = 2,
            roomsMin = 1,
            stateFilter = ApartmentStateFilter.AVAILABLE
        ))
    }

    @Test
    fun shouldApplyFiltersForRealtor() {
        val apartment = listOf(Apartment(1, "Top Apartment", "Really awesome aparment", BigDecimal.valueOf(100),
            BigDecimal.valueOf(2000), 4, 48.532976, 14.610996, System.currentTimeMillis(),
            1, "bkraszewski@gmail.com", ApartmentState.AVAILABLE))

        whenever(apiRepository.getApartments()).thenReturn(Single.just(apartment))
        whenever(apiRepository.getApartments(any())).thenReturn(Single.just(apartment))
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(anyInt())).thenReturn("%s")
        whenever(apiRepository.observeApartmentChanges()).thenReturn(Observable.never())

        cut.setup(view)
        cut.onAttach(view)

        cut.minSize.set(100)
        cut.maxSize.set(200)
        cut.minRooms.set(1)
        cut.maxRooms.set(2)
        cut.minPrice.set(1)
        cut.maxPrice.set(1001)
        cut.selectedStateIndex.set(1)
        cut.onApplyFilters()

        verify(apiRepository).getApartments(Filters(priceMin = BigDecimal.valueOf(1),
            priceMax = BigDecimal.valueOf(1001),
            areaMax = 200,
            areaMin = 100,
            roomsMax = 2,
            roomsMin = 1,
            stateFilter = ApartmentStateFilter.RENTED
        ))
    }
}
