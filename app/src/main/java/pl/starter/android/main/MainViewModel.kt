package pl.starter.android.main

import androidx.databinding.ObservableField
import pl.starter.android.R
import pl.starter.android.base.BaseView
import pl.starter.android.base.BaseViewModel
import pl.starter.android.utils.StringProvider
import javax.inject.Inject

interface MainView : BaseView

class MainViewModel @Inject constructor(
    private val stringProvider: StringProvider
) : BaseViewModel<MainView>() {

    val helloText = ObservableField<String>("")

    override fun onAttach(view: MainView) {
        super.onAttach(view)

        helloText.set(stringProvider.getString(R.string.hello_world))
    }
}
