package com.x930073498.features.core

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import com.x930073498.lifecycle.core.InitializerScope
import com.x930073498.lifecycle.core.Removable

data class FeatureData<C, F : IFeature>(val component: C, val context: Context, val feature: F) {

    val activity: Activity? = component as? Activity
    val fragment: Fragment? = component as? Fragment
    val application: Application? = component as? Application


    fun InitializerScope.addLifecycleObserver(observer: LifecycleObserver): Removable {
        return when (component) {
            is Activity -> component.addLifecycleObserver(observer)
            is Fragment -> component.addLifecycleObserver(observer)
            is Application -> addApplicationLifecycleObserver(observer)
            else -> Removable
        }
    }
}

fun interface FeatureAction<C, F : IFeature> {
    fun FeatureData<C, F>.action(scope: InitializerScope)
}

operator fun <C, F : IFeature> FeatureAction<C, F>.plus(action: FeatureAction<C, F>) =
    FeatureAction<C, F> {
        action(it)
        with(action) {
            action(it)
        }
    }