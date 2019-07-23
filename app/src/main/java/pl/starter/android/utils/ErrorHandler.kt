package pl.starter.android.utils

import pl.starter.android.R
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface ErrorHandler {
    fun handleError(throwable: Throwable?, execute: (errorMessage: String) -> Unit = {})
}

class ErrorHandlerImpl(private val stringProvider: StringProvider) : ErrorHandler {
    override fun handleError(throwable: Throwable?, execute: (errorMessage: String) -> Unit) {
        // TODO handle errorrs
        Timber.e(throwable)
        execute(getErrorMessage(throwable))
    }

    private fun getErrorMessage(throwable: Throwable?): String {
        return when (throwable) {
            is UnknownHostException -> stringProvider.getString(R.string.generic_error_offline)
            is SocketTimeoutException -> stringProvider.getString(R.string.generic_error_timeout)
            else -> throwable?.localizedMessage
                ?: stringProvider.getString(R.string.generic_error_unknown)
        }
    }
}
