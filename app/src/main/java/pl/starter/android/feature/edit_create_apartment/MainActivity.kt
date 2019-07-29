package pl.starter.android.feature.edit_create_apartment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import pl.starter.android.R
import pl.starter.android.base.BaseActivity
import pl.starter.android.databinding.ActivityMainBinding
import pl.starter.android.feature.auth.login.LoginFragment
import pl.starter.android.feature.auth.register.RegisterFragment
import pl.starter.android.feature.explore.ExploreFragment
import pl.starter.android.feature.profile.ProfileFragment
import pl.starter.android.service.Apartment

class EditCreateApartmentActivity : BaseActivity<EditCreateApartmentView, EditCreateApartmentViewModel, ActivityMainBinding>(), EditCreateApartmentView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setup(R.layout.activity_create_edit_aparment, this, EditCreateApartmentViewModel::class.java)
    }



    companion object {
        const val APARTMENT = "apartment"

        fun startForNew(context: Context) {
            context.startActivity(Intent(context, EditCreateApartmentActivity::class.java))
        }

        fun startForEdit(context: Context, apartment:Apartment) {
            context.startActivity(Intent(context, EditCreateApartmentActivity::class.java).apply {
                putExtra(APARTMENT, apartment)
            })
        }
    }


}
