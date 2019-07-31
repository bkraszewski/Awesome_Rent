package pl.starter.android.base

import android.app.Activity
import android.app.Application
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import pl.starter.android.di.AppComponent
import pl.starter.android.di.AppInjector
import javax.inject.Inject

class BaseApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
        initDagger()
    }

    fun getAppComponent(): AppComponent {
        return appComponent
    }

    private fun initDagger() {
        appComponent = AppInjector.init(this)
    }

    companion object {
        lateinit var appComponent: AppComponent
    }
}
