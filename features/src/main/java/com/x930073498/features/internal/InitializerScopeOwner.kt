package com.x930073498.features.internal

import android.app.Application
import androidx.fragment.app.FragmentManager
import com.x930073498.features.core.InitializerScope

internal class InitializerScopeOwner {
    private val scopes = LockList.create<InitializerScopeImpl>()
    fun addScope(scope: InitializerScope) {
        if (scope is InitializerScopeImpl) scopes.doOnLock {
            add(scope)
        }
    }
   internal fun doOnActivityLifecycle(action:Application.ActivityLifecycleCallbacks.()->Unit){
        scopes.forEach {
            this.doOnActivityLifecycle(action)
        }
    }

   internal fun doOnFragmentLifecycle(action:FragmentManager.FragmentLifecycleCallbacks.()->Unit){
        scopes.forEach {
            this.doOnFragmentLifecycle(action)
        }
    }
    fun destroy() {
        scopes.doOnLock {
            forEach {
                it.destroy()
            }
            clear()
        }
    }
}