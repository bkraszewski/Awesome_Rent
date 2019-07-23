package pl.starter.android.base

interface BaseView {
    fun showMessage(message: String?)
    fun showMessage(stringResId: Int, arg: String)
    fun showMessage(stringResId: Int)
}
