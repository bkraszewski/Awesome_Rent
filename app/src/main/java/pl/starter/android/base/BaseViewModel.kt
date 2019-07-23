package pl.starter.android.base

import androidx.databinding.ObservableBoolean
import com.byoutline.secretsauce.lifecycle.AttachableViewModelRx
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import pl.starter.android.utils.ErrorHandler
import javax.inject.Inject

open class BaseViewModel<T : BaseView> : AttachableViewModelRx<T>() {

    protected val disposable: CompositeDisposable = CompositeDisposable()
    @Inject
    lateinit var errorHandler: ErrorHandler

    val inProgress = ObservableBoolean(false)

    protected val defaultErrorConsumer: Consumer<Throwable> = Consumer { defaultErrorAction(it) }

    protected val defaultErrorAction: ((throwable: Throwable) -> Unit) = { throwable: Throwable ->
        inProgress.set(false)
        errorHandler.handleError(throwable) { view?.showMessage(it) }
    }


    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
