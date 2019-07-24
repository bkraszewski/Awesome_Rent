package pl.starter.android.utils

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler

class TestSchedulerImpl :BaseSchedulers{
    override fun io(): Scheduler = Schedulers.trampoline()


    override fun computation(): Scheduler= Schedulers.trampoline()

    override fun main(): Scheduler = Schedulers.trampoline()

    override fun single(): Scheduler = Schedulers.trampoline()

}
