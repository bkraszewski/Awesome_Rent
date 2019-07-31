package pl.starter.android.base


import android.view.Menu
import androidx.annotation.MenuRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.byoutline.secretsauce.databinding.bindContentView
import com.byoutline.secretsauce.di.Injectable
import com.byoutline.secretsauce.lifecycle.ViewModelAutoLifecycleA
import pl.starter.android.BR
import javax.inject.Inject

open class BaseActivity<VIEW : BaseView, VM : BaseViewModel<VIEW>, BINDING : ViewDataBinding>
    : AppCompatActivity(), BaseView, Injectable {

    @MenuRes
    open val menuResId: Int? = null

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    lateinit var binding: BINDING
    lateinit var viewModel: VM

    fun setup(layoutId: Int, view: VIEW, clazz: Class<VM>) {
        binding = bindContentView(layoutId)
        viewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(clazz)
        binding.setVariable(BR.viewModel, viewModel)
        application.registerActivityLifecycleCallbacks(
            ViewModelAutoLifecycleA(
                application,
                view,
                viewModel
            )
        )
    }


    override fun showMessage(message: String?) {
        message?.let {
            //toast(it)
        }
    }

    override fun showMessage(stringResId: Int) {
        showMessage(getString(stringResId))
    }

    override fun showMessage(stringResId: Int, arg: String) {
        showMessage(getString(stringResId, arg))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (menuResId != null) menuInflater.inflate(menuResId!!, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
