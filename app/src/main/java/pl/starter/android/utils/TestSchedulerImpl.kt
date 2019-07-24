package pl.starter.android.utils

import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

class TestSchedulerImpl :BaseSchedulers{
    override fun io(): Scheduler = TestScheduler()


    override fun computation(): Scheduler= TestScheduler()

    override fun main(): Scheduler = TestScheduler()

    override fun single(): Scheduler = TestScheduler()

}
