package com.x930073498.features.core.fragment

import com.x930073498.features.core.FeatureLifecycle

interface FragmentFeatureLifecycle :FeatureLifecycle{
    fun addObserver(observer: FragmentFeatureLifecycleObserver)
    fun removeObserver(observer: FragmentFeatureLifecycleObserver)

}