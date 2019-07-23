package pl.starter.android.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

import com.byoutline.secretsauce.di.Injectable
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import pl.starter.android.base.BaseApp

object AppInjector {

    fun init(
        app: BaseApp,
        graphCreator: (BaseApp) -> AppComponent = { buildGraph(it) }
    ): AppComponent {

        val appComponent = graphCreator(app)

        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                handleActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
        return appComponent
    }

    private fun buildGraph(app: BaseApp): AppComponent {
        val appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(app))
            .build()
        appComponent.inject(app)
        return appComponent
    }

    private fun handleActivity(activity: Activity) {
        if (activity is HasSupportFragmentInjector || activity is Injectable) {
            AndroidInjection.inject(activity)
            (activity as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentCreated(
                        fm: FragmentManager,
                        f: Fragment,
                        savedInstanceState: Bundle?
                    ) {
                        if (f is Injectable) {
                            AndroidSupportInjection.inject(f)
                        }
                    }
                }, true
            )
        }
    }
}
