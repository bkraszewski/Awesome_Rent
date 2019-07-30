package pl.starter.android.feature.explore

import com.nhaarman.mockitokotlin2.isA
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
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

        whenever(apiRepository.getApartments()).thenReturn(Single.just(apartment))
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com"))
        whenever(stringProvider.getString(anyInt())).thenReturn("%s")

        cut.onAttach(view)

        assertThat(cut.apartments.size, Is(equalTo(1)))
    }
}
