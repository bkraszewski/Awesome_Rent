package pl.starter.android.feature.edit_create_apartment

import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.hamcrest.CoreMatchers.not
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import pl.starter.android.service.*
import pl.starter.android.utils.BaseSchedulers
import pl.starter.android.utils.StringProvider
import pl.starter.android.utils.TestSchedulerImpl
import java.math.BigDecimal
import org.mockito.ArgumentCaptor
import java.lang.Exception


class EditCreateApartmentViewModelTest {

    lateinit var cut: EditCreateApartmentViewModel
    lateinit var userRepository: UserRepository
    lateinit var apiRepository: ApiRepository
    lateinit var baseSchedulers: BaseSchedulers
    lateinit var stringProvider: StringProvider
    lateinit var view: EditCreateApartmentView

    @Before
    fun setUp() {
        userRepository = mock()
        apiRepository = mock()
        baseSchedulers = TestSchedulerImpl()
        stringProvider = mock()
        view = mock()

        cut = EditCreateApartmentViewModel(userRepository, stringProvider, apiRepository, baseSchedulers)
    }

    @Test
    fun formShouldBeBlankForNewApartment() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))

        cut.onAttach(view)
        cut.onNewApartment()

        assertThat(cut.apartmentName.get(), IsEqual(""))
        assertThat(cut.apartmentDescription.get(), IsEqual(""))
        assertThat(cut.apartmentAreaSize.get(), IsEqual(""))
        assertThat(cut.apartmentLat.get(), IsEqual(""))
        assertThat(cut.apartmentLng.get(), IsEqual(""))
        assertThat(cut.apartmentMonthlyPrice.get(), IsEqual(""))
        assertThat(cut.apartmentRoomsCount.get(), IsEqual(""))
        assertThat(cut.apartmentStatusIndex.get(), IsEqual(0))

    }

    @Test
    fun formShouldBeEditableForRealtorWhenAddingNew() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))

        cut.onAttach(view)
        cut.onNewApartment()

        assertThat(cut.canChangeRealtor.get(), IsEqual(true))
        assertThat(cut.canSaveChanges.get(), IsEqual(true))
        assertThat(cut.canChangeLocation.get(), IsEqual(true))
        assertThat(cut.canChangeStatus.get(), IsEqual(true))
    }

    @Test
    fun shouldValidateFormFields1() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")

        cut.onAttach(view)
        cut.onNewApartment()


        cut.apartmentName.set("")
        cut.onSave()
        assertThat(cut.apartmentNameError.get(), not(IsEqual("")))
    }

    @Test
    fun shouldValidateFormFields2() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")

        cut.onAttach(view)
        cut.onNewApartment()

        cut.apartmentName.set("Hello")
        cut.onSave()

        assertThat(cut.apartmentNameError.get(), IsEqual(""))
    }

    @Test
    fun shouldValidateFormFields3() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")

        cut.onAttach(view)
        cut.onNewApartment()

        cut.apartmentName.set("Hello")
        cut.apartmentDescription.set("")
        cut.onSave()

        assertThat(cut.apartmentDescriptionError.get(), not(IsEqual("")))
    }

    @Test
    fun shouldValidateFormFields4() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")

        cut.onAttach(view)
        cut.onNewApartment()

        cut.apartmentName.set("Hello")
        cut.apartmentDescription.set("World")
        cut.onSave()

        assertThat(cut.apartmentDescriptionError.get(), IsEqual(""))
    }

    @Test
    fun shouldValidateFormFields5() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")

        cut.onAttach(view)
        cut.onNewApartment()

        cut.apartmentName.set("Hello")
        cut.apartmentDescription.set("World")
        cut.apartmentRoomsCount.set("")
        cut.onSave()

        assertThat(cut.apartmentRoomsCountError.get(), not(IsEqual("")))
    }

    @Test
    fun shouldValidateFormFields6() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")

        cut.onAttach(view)
        cut.onNewApartment()

        cut.apartmentName.set("Hello")
        cut.apartmentDescription.set("World")
        cut.apartmentRoomsCount.set("4")
        cut.onSave()

        assertThat(cut.apartmentRoomsCountError.get(), IsEqual(""))
    }

    @Test
    fun shouldValidateFormFields7() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")

        cut.onAttach(view)
        cut.onNewApartment()

        cut.apartmentName.set("Hello")
        cut.apartmentDescription.set("World")
        cut.apartmentRoomsCount.set("4")
        cut.apartmentAreaSize.set("")
        cut.onSave()

        assertThat(cut.apartmentAreaSizeError.get(), not(IsEqual("")))
    }

    @Test
    fun shouldValidateFormFields8() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")

        cut.onAttach(view)
        cut.onNewApartment()

        cut.apartmentName.set("Hello")
        cut.apartmentDescription.set("World")
        cut.apartmentRoomsCount.set("4")
        cut.apartmentAreaSize.set("42.5")
        cut.onSave()

        assertThat(cut.apartmentAreaSizeError.get(), IsEqual(""))
    }

    @Test
    fun shouldValidateFormFields9Lat() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")

        cut.onAttach(view)
        cut.onNewApartment()

        cut.apartmentName.set("Hello")
        cut.apartmentDescription.set("World")
        cut.apartmentRoomsCount.set("4")
        cut.apartmentAreaSize.set("21")
        cut.apartmentLat.set("")
        cut.onSave()

        assertThat(cut.apartmentLatError.get(), not(IsEqual("")))
    }

    @Test
    fun shouldValidateFormFields10Lat() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")

        cut.onAttach(view)
        cut.onNewApartment()

        cut.apartmentName.set("Hello")
        cut.apartmentDescription.set("World")
        cut.apartmentRoomsCount.set("4")
        cut.apartmentAreaSize.set("42.5")
        cut.apartmentLat.set("43.332453")
        cut.onSave()

        assertThat(cut.apartmentLatError.get(), IsEqual(""))
    }

    @Test
    fun shouldValidateFormFields11Lng() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")

        cut.onAttach(view)
        cut.onNewApartment()

        cut.apartmentName.set("Hello")
        cut.apartmentDescription.set("World")
        cut.apartmentRoomsCount.set("4")
        cut.apartmentAreaSize.set("21")
        cut.apartmentLat.set("32.32432")
        cut.apartmentLng.set("")
        cut.onSave()

        assertThat(cut.apartmentLngError.get(), not(IsEqual("")))
    }

    @Test
    fun shouldValidateFormFields12Lng() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")

        cut.onAttach(view)
        cut.onNewApartment()

        cut.apartmentName.set("Hello")
        cut.apartmentDescription.set("World")
        cut.apartmentRoomsCount.set("4")
        cut.apartmentAreaSize.set("42.5")
        cut.apartmentLat.set("43.332453")
        cut.apartmentLng.set("33.23453")
        cut.onSave()

        assertThat(cut.apartmentLngError.get(), IsEqual(""))
    }

    @Test
    fun shouldValidateFormFields13Price() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")

        cut.onAttach(view)
        cut.onNewApartment()

        cut.apartmentName.set("Hello")
        cut.apartmentDescription.set("World")
        cut.apartmentRoomsCount.set("4")
        cut.apartmentAreaSize.set("21")
        cut.apartmentLat.set("32.32432")
        cut.apartmentLng.set("32.32453")
        cut.apartmentMonthlyPrice.set("")
        cut.onSave()

        assertThat(cut.apartmentMonthlyPriceError.get(), not(IsEqual("")))
    }

    @Test
    fun shouldValidateFormFields14Price() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")
        whenever(apiRepository.createApartment(any())).thenReturn(Single.error(Exception("Does not matter now")))

        cut.onAttach(view)
        cut.onNewApartment()

        cut.apartmentName.set("Hello")
        cut.apartmentDescription.set("World")
        cut.apartmentRoomsCount.set("4")
        cut.apartmentAreaSize.set("42.5")
        cut.apartmentLat.set("43.332453")
        cut.apartmentLng.set("33.23453")
        cut.apartmentMonthlyPrice.set("200")
        cut.onSave()

        assertThat(cut.apartmentMonthlyPriceError.get(), IsEqual(""))
    }

    @Test
    fun shouldCreateNewApartment() {
        whenever(userRepository.getUser()).thenReturn(User(1, "bkraszewski@gmail.com", Role.REALTOR))
        whenever(stringProvider.getString(any())).thenReturn("hello world")
        whenever(apiRepository.createApartment(any())).thenReturn(Single.error(Exception("Not important now")))
        //val argument = ArgumentCaptor.forClass(Apartment::class.java)



        cut.onAttach(view)
        cut.onNewApartment()

        cut.apartmentName.set("Hello")
        cut.apartmentDescription.set("World")
        cut.apartmentRoomsCount.set("4")
        cut.apartmentAreaSize.set("42.5")
        cut.apartmentLat.set("43.332453")
        cut.apartmentLng.set("33.23453")
        cut.apartmentMonthlyPrice.set("200")
        cut.addedTimestamp = 2000
        cut.onSave()

//        verify(apiRepository).createApartment(argument.capture())

        val apartment = Apartment(id=-1, name="Hello", description = "World",
            floorAreaSize = BigDecimal.valueOf(42.5),realtorEmail = "bkraszewski@gmail.com", realtorId = 1,
            latitude =43.332453, longitude = 33.23453, pricePerMonth = BigDecimal.valueOf(200),
            rooms = 4, state = ApartmentState.AVAILABLE, addedTimestamp = 2000)

//        assertThat(apartment, IsEqual(argument.value))
        verify(apiRepository).createApartment(apartment)
    }

}
