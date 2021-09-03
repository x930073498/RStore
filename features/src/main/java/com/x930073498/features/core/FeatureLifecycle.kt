package com.x930073498.features.core

import com.x930073498.features.core.activity.ActivityFeatureLifecycle
import com.x930073498.features.core.application.ApplicationFeatureLifecycle
import com.x930073498.features.core.fragment.FragmentFeatureLifecycle

interface FeatureLifecycle:FragmentFeatureLifecycle ,ActivityFeatureLifecycle,ApplicationFeatureLifecycle{
    fun addObserver(observer: FeatureLifecycleObserver)
    fun removeObserver(observer: FeatureLifecycleObserver)
}


