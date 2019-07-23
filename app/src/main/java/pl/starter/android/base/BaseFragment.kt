package pl.starter.android.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.byoutline.secretsauce.lifecycle.AttachableViewModel
import com.byoutline.secretsauce.lifecycle.ViewModelAutoLifecycleF
import pl.starter.android.BR

@SuppressLint("ValidFragment")
open class BaseFragment<VIEW : BaseView, VM : BaseViewModel<VIEW>, BINDING : ViewDataBinding>(
    private val layoutId: Int
) : Fragment(), BaseView {

    lateinit var binding: BINDING
    lateinit var viewModel: VM

    private var lifeCycleCallback: ViewModelAutoLifecycleF<AttachableViewModel<VIEW>, VIEW>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return binding.root
    }


    fun setup(view: VIEW, clazz: Class<VM>, hasActivityLifespan: Boolean = true) {
        val factory = (requireActivity().application as BaseApp).getAppComponent().getViewModelProviderFactory()
        viewModel = if (hasActivityLifespan) {
            ViewModelProviders.of(requireActivity(), factory).get(clazz)
        } else {
            ViewModelProviders.of(this, factory).get(clazz)
        }
        binding.setVariable(BR.viewModel, viewModel)
        lifeCycleCallback = ViewModelAutoLifecycleF(
            view,
            viewModel
        )
        requireActivity().supportFragmentManager.registerFragmentLifecycleCallbacks(
            lifeCycleCallback as ViewModelAutoLifecycleF<VM, VIEW>, true
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unbind()
        lifeCycleCallback?.let {
            requireActivity().supportFragmentManager.unregisterFragmentLifecycleCallbacks(it)
        }
    }

    override fun showMessage(message: String?) {
        message?.let {
            Toast.makeText(baseActivity, it, Toast.LENGTH_LONG).show()
        }
    }

    val baseActivity: BaseActivity<*, *, *>
        get() = activity!! as BaseActivity<*, *, *>

    override fun showMessage(stringResId: Int) {
        showMessage(getString(stringResId))
    }

    override fun showMessage(stringResId: Int, arg: String) {
        showMessage(getString(stringResId, arg))
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isResumed) onVisibilityChange(isVisibleToUser)
    }

    override fun onResume() {
        super.onResume()
        if (userVisibleHint) onVisibilityChange(true)
    }

    open fun onVisibilityChange(isVisible: Boolean) {}
}
