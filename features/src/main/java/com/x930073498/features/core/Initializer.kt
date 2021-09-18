package com.x930073498.features.core

import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleObserver
import com.x930073498.features.internal.DEFAULT

fun interface Initializer {
    fun InitializerScope.setup(target: FeatureTarget)
}

fun InitializerScope.isDefault(): Boolean = this === DEFAULT
interface InitializerScope {


    fun destroy()


    fun FeatureTarget.FragmentTarget.addObserver(observer: FragmentFeatureLifecycleObserver):Removable

    fun FeatureTarget.FragmentTarget.addObserver(
        observer: LifecycleObserver,
        bindView: Boolean = true
    ):Removable

    fun FeatureTarget.ActivityTarget.addObserver(observer: ActivityFeatureLifecycleObserver):Removable


    @RequiresApi(29)
    fun FeatureTarget.ActivityTarget.addObserver(observer: ActivityFeatureStateObserver):Removable

    fun FeatureTarget.ActivityTarget.addObserver(observer: LifecycleObserver):Removable

    fun FeatureTarget.ApplicationTarget.addObserver(observer: ApplicationFeatureLifecycleObserver):Removable
    fun FeatureTarget.ApplicationTarget.addObserver(observer: LifecycleObserver):Removable


}
