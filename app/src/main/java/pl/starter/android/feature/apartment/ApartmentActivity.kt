package pl.starter.android.feature.apartment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
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

    override fun onStart() {
        super.onStart()

        val apartment = intent.getParcelableExtra<Apartment?>(APARTMENT)

        if (apartment == null) {
            viewModel.onNewApartment()
        } else {
            viewModel.onShowExistingApartment(apartment)
        }
    }

    override fun onResume() {
        super.onResume()
        android.os.Handler().post {
            statusSpinner.setSelection(viewModel.apartmentStatusIndex.get())
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
