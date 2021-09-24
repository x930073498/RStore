package com.x930073498.lifecycle.core

import android.app.Activity
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import com.x930073498.lifecycle.internal.addActivityLifecycleObserverImpl
import com.x930073498.lifecycle.internal.addFragmentLifecycleObserverImpl
import com.x930073498.lifecycle.internal.addFragmentViewLifecycleObserverImpl

interface InitializerScope : Removable,ScopeEnvironment {


    @RequiresApi(29)
    fun addLifecycleCallback(lifecycleCallback: ActivityLifecycleCallback){
        addLifecycleCallback(lifecycleCallback as LifecycleCallback)
    }
    fun addLifecycleCallback(lifecycleCallback: LifecycleCallback): Removable

    fun addApplicationLifecycleObserver(observer: LifecycleObserver): Removable

    fun Activity.addLifecycleObserver(observer: LifecycleObserver): Removable

    fun Fragment.addLifecycleObserver(observer: LifecycleObserver): Removable

    fun Fragment.addViewLifecycleObserver(observer: LifecycleObserver): Removable

}

fun InitializerScope.addActivityLifecycleObserver(observer: LifecycleObserver) =
    addActivityLifecycleObserverImpl(observer)

fun InitializerScope.addFragmentLifecycleObserver(observer: LifecycleObserver) =
    addFragmentLifecycleObserverImpl(observer)
fun InitializerScope.addFragmentViewLifecycleObserver(observer: LifecycleObserver) =
    addFragmentViewLifecycleObserverImpl(observer)