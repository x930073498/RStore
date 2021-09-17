package com.x930073498.features.core.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * 带有pre/post开头的方法需要android 10及以上的机型支持
 */

interface ActivityFeatureLifecycleObserver {
    companion object : ActivityFeatureLifecycleObserver

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



}