@file:JvmName("Features")

package com.x930073498.lifecycle

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.startup.AppInitializer
import com.x930073498.lifecycle.core.Initializer
import com.x930073498.lifecycle.core.Removable
import com.x930073498.lifecycle.internal.ActivityLifecycleOwner
import com.x930073498.lifecycle.internal.LifecycleException
import com.x930073498.lifecycle.internal.InitializerScopeImpl

val application by ::app

fun setupComponent(context: Context) {
    AppInitializer.getInstance(context).initializeComponent(FeatureStartup::class.java)
}

fun LifecycleOwner.castActivity(): Activity {
    return activity
        ?: throw LifecycleException("LifecycleOwner 【$this】 不是附着于activity的LifecycleOwner")
}

val LifecycleOwner.activity: Activity?
    get() {
        return if (this is Activity) this else if (this is ActivityLifecycleOwner) activity else null
    }

fun addInitializer(fromNowOn: Boolean = true, initializer: Initializer): Removable {
    val scope = InitializerScopeImpl()
    with(initializer) {
        if (fromNowOn) scope.callInit()
        scope.setup()
        if (!fromNowOn) scope.callInit()
    }
    return scope
}