package pl.starter.android.feature.edit_create_apartment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import pl.starter.android.R
import pl.starter.android.base.BaseActivity
import pl.starter.android.databinding.ActivityMainBinding
import pl.starter.android.service.Apartment

class EditCreateApartmentActivity : BaseActivity<EditCreateApartmentView, EditCreateApartmentViewModel, ActivityMainBinding>(), EditCreateApartmentView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup(R.layout.activity_create_edit_aparment, this, EditCreateApartmentViewModel::class.java)

        val apartment = intent.getParcelableExtra<Apartment?>("Apartment")

        if (apartment == null) {
            viewModel.onNewApartment()
        }
    }


    companion object {
        const val APARTMENT = "apartment"

        fun startForNew(context: Context) {
            context.startActivity(Intent(context, EditCreateApartmentActivity::class.java))
        }

        fun startForEdit(context: Context, apartment: Apartment) {
            context.startActivity(Intent(context, EditCreateApartmentActivity::class.java).apply {
                putExtra(APARTMENT, apartment)
            })
        }
    }


}
