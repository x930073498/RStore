package com.x930073498.features.core.activity

import androidx.annotation.RequiresApi
import com.x930073498.features.core.FeatureLifecycle

interface ActivityFeatureLifecycle:FeatureLifecycle {

    @RequiresApi(29)
    fun addObserver(observer: ActivityFeatureStateObserver){
        addObserver(observer as ActivityFeatureLifecycleObserver)
    }

    @RequiresApi
    fun removeObserver(observer: ActivityFeatureStateObserver){
        removeObserver(observer as ActivityFeatureLifecycleObserver)
    }
    fun addObserver(observer: ActivityFeatureLifecycleObserver)
    fun removeObserver(observer: ActivityFeatureLifecycleObserver)
}