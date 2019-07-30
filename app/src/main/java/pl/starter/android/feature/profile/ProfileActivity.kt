package pl.starter.android.feature.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.byoutline.secretsauce.activities.showFragment
import kotlinx.android.synthetic.main.activity_profile.*
import pl.starter.android.R
import pl.starter.android.service.User

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val createNew = intent.getBooleanExtra(CREATE_USER, false)

        if (createNew) {
            showFragment(ProfileFragment.newInstanceForNewUser(),
                containerViewId = R.id.fragmentContainer,
                addToBackStack = false)
        }

        closeIcon.setOnClickListener {
            finish()
        }
    }

    companion object {

        const val USER = "user"
        const val CREATE_USER = "createUser"

        fun startForEdit(context: Context, user: User) {
            context.startActivity(Intent(context, ProfileActivity::class.java).apply {
                putExtra(USER, user)
            })
        }

        fun startForNewUser(context: Context) {
            context.startActivity(Intent(context, ProfileActivity::class.java).apply {
                putExtra(CREATE_USER, true)
            })
        }
    }
}
