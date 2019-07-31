package pl.starter.android.feature.apartment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.sucho.placepicker.AddressData
import com.sucho.placepicker.Constants
import com.sucho.placepicker.PlacePicker
import kotlinx.android.synthetic.main.activity_apartment.*
import pl.starter.android.R
import pl.starter.android.base.BaseActivity
import pl.starter.android.databinding.ActivityApartmentBinding
import pl.starter.android.service.Apartment

class ApartmentActivity : BaseActivity<ApartmentView, ApartmentViewModel, ActivityApartmentBinding>(), ApartmentView {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup(R.layout.activity_apartment, this, ApartmentViewModel::class.java)
        intiUi()
        initData()
    }

    private fun initData() {
        val apartment = intent.getParcelableExtra<Apartment?>(APARTMENT)
        viewModel.initialize()

        if (apartment == null) {
            viewModel.onNewApartment()
        } else {
            viewModel.onShowExistingApartment(apartment)
        }
    }

    private fun intiUi() {
        deleteApartmentIcon.setOnClickListener {
            showConfirmationDialog()
        }
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.form_confirm_delete_title)
            .setMessage(R.string.form_confirm_delete_message)
            .setPositiveButton(R.string.common_yes) { dialog, which ->
                viewModel.onDelete()
            }.setNegativeButton(R.string.common_no) { dialog, which -> dialog.dismiss() }
            .show()
    }


    override fun onResume() {
        super.onResume()
        android.os.Handler().post {
            statusSpinner.setSelection(viewModel.apartmentStatusIndex.get())
        }
    }

    override fun showLocationPicker(lat: Double, lng: Double) {
        val intent = PlacePicker.IntentBuilder()
            .setLatLong(lat, lng)
            .showLatLong(true)
            .setMapZoom(5.0f)
            .setAddressRequired(false)
            .setMarkerImageImageColor(R.color.colorPrimary)
            .setFabColor(R.color.colorPrimary)
            .setPrimaryTextColor(R.color.colorPrimary)
            .setSecondaryTextColor(R.color.black)
            .build(this)
        startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val addressData = data?.getParcelableExtra<AddressData>(Constants.ADDRESS_INTENT)
                addressData?.let {
                    viewModel.apartmentLat.set(it.latitude.toString())
                    viewModel.apartmentLng.set(it.longitude.toString())
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    companion object {
        const val APARTMENT = "apartment"

        fun startForNew(context: Context) {
            context.startActivity(Intent(context, ApartmentActivity::class.java))
        }

        fun startForView(context: Context, apartment: Apartment) {
            context.startActivity(Intent(context, ApartmentActivity::class.java).apply {
                putExtra(APARTMENT, apartment)
            })
        }
    }


}
