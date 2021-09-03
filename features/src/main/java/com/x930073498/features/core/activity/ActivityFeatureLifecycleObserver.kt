package com.x930073498.features.core.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface ActivityFeatureLifecycleObserver {
    companion object:ActivityFeatureLifecycleObserver
    fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    fun onActivityStarted(activity: Activity) {

    }

    fun onActivityResumed(activity: Activity) {

    }

    fun onActivityPaused(activity: Activity) {

    }

    fun onActivityStopped(activity: Activity) {

    }

    fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    fun onActivityDestroyed(activity: Activity) {

    }

    @RequiresApi(29)
    fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
    }
    @RequiresApi(29)
    fun onActivityPostCreated(activity: Activity, savedInstanceState: Bundle?) {
    }
    @RequiresApi(29)
    fun onActivityPreStarted(activity: Activity) {
    }

    @RequiresApi(29)
    fun onActivityPostStarted(activity: Activity) {
    }

    @RequiresApi(29)
    fun onActivityPreResumed(activity: Activity) {
    }

    @RequiresApi(29)
    fun onActivityPostResumed(activity: Activity) {
    }
    @RequiresApi(29)
    fun onActivityPrePaused(activity: Activity) {
    }
    @RequiresApi(29)
    fun onActivityPostPaused(activity: Activity) {
    }
    @RequiresApi(29)
    fun onActivityPreStopped(activity: Activity) {
    }
    @RequiresApi(29)
    fun onActivityPostStopped(activity: Activity) {
    }
    @RequiresApi(29)
    fun onActivityPreSaveInstanceState(activity: Activity, outState: Bundle) {
    }
    @RequiresApi(29)
    fun onActivityPostSaveInstanceState(activity: Activity, outState: Bundle) {
    }
    @RequiresApi(29)
    fun onActivityPreDestroyed(activity: Activity) {
    }
    @RequiresApi(29)
    fun onActivityPostDestroyed(activity: Activity) {
    }


}