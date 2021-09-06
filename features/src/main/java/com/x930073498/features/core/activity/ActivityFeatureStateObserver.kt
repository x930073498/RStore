package com.x930073498.features.core.activity

import android.app.Activity
import android.os.Bundle
import androidx.annotation.RequiresApi


@RequiresApi(29)
interface ActivityFeatureStateObserver:ActivityFeatureLifecycleObserver {


    fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    fun onActivityPreStarted(activity: Activity) {
    }

    fun onActivityPostStarted(activity: Activity) {
    }

    fun onActivityPreResumed(activity: Activity) {
    }

    fun onActivityPostResumed(activity: Activity) {
    }

    fun onActivityPrePaused(activity: Activity) {
    }

    fun onActivityPostPaused(activity: Activity) {
    }

    fun onActivityPreStopped(activity: Activity) {
    }

    fun onActivityPostStopped(activity: Activity) {
    }

    fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
    }

    fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
    }

    fun onActivityPreDestroyed(activity: Activity) {
    }

    fun onActivityPostDestroyed(activity: Activity) {
    }
}