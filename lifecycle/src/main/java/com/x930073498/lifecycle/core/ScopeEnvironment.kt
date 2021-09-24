package com.x930073498.lifecycle.core

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner

interface ScopeEnvironment {
    abstract val application: Application
    abstract val activities: List<Activity>
    abstract val fragments: List<Fragment>
    abstract val processLifecycleOwner: LifecycleOwner

//     abstract fun Removable(activity: Activity, action: () -> Unit): Removable
//
//     abstract fun Removable(fragment: Fragment, action: () -> Unit): Removable
//
//     abstract fun Removable(application: Application, action: () -> Unit): Removable

}