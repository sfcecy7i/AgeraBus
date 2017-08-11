package com.chinamobile.cmos.agera.bus

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.google.android.agera.Reservoir
import com.google.android.agera.Reservoirs
import com.google.android.agera.Updatable

object ABus {
    private lateinit var bus: Reservoir<Any>
    private lateinit var busCallbacks: BusActivityLifecycleCallbacks
    private lateinit var responders: MutableSet<(Any) -> Unit>
    private lateinit var responderMapCreate: MutableMap<Activity, MutableSet<(Any) -> Unit>>
    private lateinit var responderMapStart: MutableMap<Activity, MutableSet<(Any) -> Unit>>
    private lateinit var responderMapResume: MutableMap<Activity, MutableSet<(Any) -> Unit>>

    fun get(): Any? {
        val value = bus.get()
        return if (value.isPresent) value.get() else null
    }

    fun post(anything: Any) {
        bus.accept(anything)
    }

    fun add(block: (Any) -> Unit) {
        responders.add(block)
    }

    fun remove(block: (Any) -> Unit) {
        responders.remove(block)
    }

    fun whileCreate(activity: Activity, block: (Any) -> Unit) {
        (responderMapCreate[activity] ?: mutableSetOf())
                .let {
                    it.add(block)
                    responderMapCreate.put(activity, it)
                    responders.add(block)
                }
    }

    fun whileStart(activity: Activity, block: (Any) -> Unit) {
        (responderMapCreate[activity] ?: mutableSetOf())
                .let {
                    it.add(block)
                    responderMapStart.put(activity, it)
                    responders.add(block)
                }
    }

    fun whileResume(activity: Activity, block: (Any) -> Unit) {
        (responderMapCreate[activity] ?: mutableSetOf())
                .let {
                    it.add(block)
                    responderMapResume.put(activity, it)
                    responders.add(block)
                }
    }

    fun activate(application: Application) {
        bus = Reservoirs.reservoir<Any>()
        busCallbacks = BusActivityLifecycleCallbacks()
        application.registerActivityLifecycleCallbacks(busCallbacks)
        bus.addUpdatable(busCallbacks)
        responders = mutableSetOf()
        responderMapCreate = mutableMapOf()
        responderMapStart = mutableMapOf()
        responderMapResume = mutableMapOf()
    }

    fun deactivate(application: Application) {
        bus.removeUpdatable(busCallbacks)
        application.unregisterActivityLifecycleCallbacks(busCallbacks)
    }

    private class BusActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks, Updatable {
        override fun update() {
            get()?.let {
                val data = it
                responders.forEach {
                    it.invoke(data)
                }
            }
        }

        override fun onActivityResumed(activity: Activity?) {
            activity?.let {
                responderMapResume[it]?.let {
                    responders.addAll(it)
                }
            }
        }

        override fun onActivityPaused(activity: Activity?) {
            activity?.let {
                responderMapResume[it]?.let {
                    responders.removeAll(it)
                }
            }
        }

        override fun onActivityStarted(activity: Activity?) {
            activity?.let {
                responderMapStart[it]?.let {
                    responders.addAll(it)
                }
            }
        }

        override fun onActivityStopped(activity: Activity?) {
            activity?.let {
                responderMapStart[it]?.let {
                    responders.removeAll(it)
                }
            }
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        }

        override fun onActivityDestroyed(activity: Activity?) {
            activity?.let {
                responderMapCreate[it]?.let {
                    responders.removeAll(it)
                    responderMapCreate.remove(activity)
                }
                responderMapStart[it]?.let {
                    responders.removeAll(it)
                    responderMapStart.remove(activity)
                }
                responderMapResume[it]?.let {
                    responders.removeAll(it)
                    responderMapResume.remove(activity)
                }
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }
    }
}
