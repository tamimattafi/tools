package com.attafitamim.tools

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.annotation.IntDef
import org.chateos.tezro.ui.activities.global.ActivityLifeCycleHandler.ActivityState.Companion.CREATED
import org.chateos.tezro.ui.activities.global.ActivityLifeCycleHandler.ActivityState.Companion.DESTROYED
import org.chateos.tezro.ui.activities.global.ActivityLifeCycleHandler.ActivityState.Companion.PAUSED
import org.chateos.tezro.ui.activities.global.ActivityLifeCycleHandler.ActivityState.Companion.RESUMED
import org.chateos.tezro.ui.activities.global.ActivityLifeCycleHandler.ActivityState.Companion.SAVE_INSTANCE_STATE
import org.chateos.tezro.ui.activities.global.ActivityLifeCycleHandler.ActivityState.Companion.STARTED
import org.chateos.tezro.ui.activities.global.ActivityLifeCycleHandler.ActivityState.Companion.STOPPED
import org.chateos.tezro.ui.activities.global.ActivityLifeCycleHandler.ActivityState.Companion.UNKNOWN

class ActivityLifeCycleHandler<T: Activity> : Application.ActivityLifecycleCallbacks {

    @ActivityState
    var currentState: Int = UNKNOWN
    private set(newState) {
        field = newState
        this.notifyStateListeners(newState)
    }

    private val stateListeners = ArrayList<(newState: Int) -> Unit>()
    private val specificStateListeners = HashMap<Int, ArrayList<() -> Unit>>()

    override fun onActivityPaused(activity: Activity) {
        this.tryChangeActivityState(activity, PAUSED)
    }

    override fun onActivityStarted(activity: Activity) {
        this.tryChangeActivityState(activity, STARTED)
    }

    override fun onActivityDestroyed(activity: Activity) {
        this.tryChangeActivityState(activity, DESTROYED)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        this.tryChangeActivityState(activity, SAVE_INSTANCE_STATE)
    }

    override fun onActivityStopped(activity: Activity) {
        this.tryChangeActivityState(activity, STOPPED)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        this.tryChangeActivityState(activity, CREATED)
    }

    override fun onActivityResumed(activity: Activity) {
        this.tryChangeActivityState(activity, RESUMED)
    }

    fun addStateChangeListener(listener: (newState: Int) -> Unit) {
        this.stateListeners.add(listener)
    }

    fun removeStateChageListener(listener: (newState: Int) -> Unit) {
        this.stateListeners.remove(listener)
    }

    fun addSpecificStateChangeListener(@ActivityState state: Int, listener: () -> Unit) {
        this.initSpecificListenersArray(state)
        specificStateListeners[state]!!.add(listener)
    }

    fun removeSpecificStateChageListener(@ActivityState state: Int, listener: () -> Unit) {
        this.initSpecificListenersArray(state)
        specificStateListeners[state]!!.remove(listener)
    }

    fun releaseListeners() {
        this.stateListeners.clear()
        this.specificStateListeners.clear()
    }

    @Suppress("UNCHECKED_CAST")
    private fun tryChangeActivityState(activity: Activity, @ActivityState state: Int) {
        val myActivity = activity as? T ?: return
        this.currentState = state
    }

    private fun notifyStateListeners(newState: Int) {
        this.stateListeners.forEach { listener -> listener.invoke(newState) }

        val specificStateListeners = this.specificStateListeners[newState]
        specificStateListeners?.forEach { listener -> listener.invoke() }
    }

    private fun initSpecificListenersArray(@ActivityState state: Int) {
        if (this.specificStateListeners[state] == null) {
            this.specificStateListeners[state] = ArrayList()
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(PAUSED, STARTED, DESTROYED, SAVE_INSTANCE_STATE, STOPPED, CREATED, RESUMED, UNKNOWN)
    annotation class ActivityState {
        companion object {
            const val PAUSED = 0
            const val STARTED = 1
            const val DESTROYED = 2
            const val SAVE_INSTANCE_STATE = 3
            const val STOPPED = 4
            const val CREATED = 5
            const val RESUMED = 6
            const val UNKNOWN = 7
        }
    }

}
