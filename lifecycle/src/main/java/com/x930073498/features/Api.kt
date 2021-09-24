package com.x930073498.features

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.x930073498.features.core.FeatureInitializer
import com.x930073498.features.internal.FeatureHandlerStoreImpl
import com.x930073498.features.internal.FeatureScopeImpl
import com.x930073498.lifecycle.activity
import com.x930073498.lifecycle.addInitializer
import com.x930073498.lifecycle.core.*

fun addFeatureInitializer(fromNowOn: Boolean = true, initializer: FeatureInitializer): Removable {
    return addInitializer(fromNowOn) {
        val handler = FeatureHandlerStoreImpl()
        val scope = FeatureScopeImpl(this, handler)
        with(initializer) {
            with(scope) {
                setup()
            }
        }
        if (fromNowOn) {
            addLifecycleCallback(object : DefaultActivityLifecycleCallback {
                override fun onActivityInit(activity: Activity, savedInstanceState: Bundle?) {
                    handler.handle(scope, activity, activity)
                }
            })
            addLifecycleCallback(object : FragmentLifecycleCallback {
                override fun onFragmentPreAttached(
                    fm: FragmentManager,
                    f: Fragment,
                    context: Context
                ) {
                    handler.handle(scope, f, context)
                }
            })
        } else {
            addApplicationLifecycleObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_CREATE) {
                        handler.handle(scope, application, application)
                    }
                }
            })
            addActivityLifecycleObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_CREATE) {
                        val activity = source.activity ?: return
                        handler.handle(scope, activity, activity)
                    }
                }
            })
            addFragmentLifecycleObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_CREATE) {
                        val fragment = source as Fragment
                        handler.handle(scope, fragment, fragment.requireActivity())
                    }
                }
            })
        }
    }
}