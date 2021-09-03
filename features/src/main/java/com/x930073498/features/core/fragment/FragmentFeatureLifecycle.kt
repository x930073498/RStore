package com.x930073498.features.core.fragment

interface FragmentFeatureLifecycle {
    fun addObserver(observer: FragmentFeatureLifecycleObserver)
    fun removeObserver(observer: FragmentFeatureLifecycleObserver)

}