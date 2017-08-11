package com.cmos.agerabus.sample

import android.app.Application
import android.util.Log
import com.chinamobile.cmos.agera.bus.ABus


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        ABus.activate(this)
    }

    override fun onTerminate() {
        Log.e("ABus", "onTerminate")
        ABus.deactivate(this)
        super.onTerminate()
    }
}