package com.x930073498.features.core

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.x930073498.features.internal.*
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Suppress("MemberVisibilityCanBePrivate")
sealed class FeatureTarget private constructor(open val data: Any) {

    private val scopeMap = mutableMapOf<Initializer, InitializerScope>()
    private val lock = ReentrantLock()
    private fun getScope(initializer: Initializer): InitializerScope? {
        return lock.withLock {
            scopeMap[initializer]
        }
    }

    internal companion object {
        private val map = mutableMapOf<Any, FeatureTarget>()
        private val lock = ReentrantLock()
        private fun get(data: Any): FeatureTarget? {
            return lock.withLock {
                map[data]
            }
        }


        private fun remove(data: Any) {
            lock.withLock {
                map.remove(data)
            }
        }

        fun requireTarget(fragment: Fragment): FragmentTarget {
            if (!fragment.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) throw FeaturesException(
                "fragment 已经销毁"
            )
            return get(fragment) as? FragmentTarget ?: FragmentTarget(
                fragment
            ).apply {
                map[fragment] = this
            }
        }

        fun requireTarget(activity: Activity): ActivityTarget {
            val owner =
                if (activity is LifecycleOwner) activity else ActivityLifecycleOwner.get(activity)
            when {
                owner == null -> throw FeaturesException("activity 已经销毁或者尚未创建")
                owner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED) -> {
                    return get(activity) as? ActivityTarget ?: ActivityTarget(activity).apply {
                        map[activity] = this
                    }
                }
                else -> {
                    throw FeaturesException("activity $activity 已经销毁")
                }
            }
        }

        fun requireTarget(application: Application):ApplicationTarget {
            return map[application] as? ApplicationTarget
                ?: ApplicationTarget(
                    application
                ).apply {
                    map[application] = this
                }
        }

        private fun getScope(data: Any, initializer: Initializer): InitializerScope? {
            return get(data)?.getScope(initializer)
        }

        private fun requireScope(data: Any, initializer: Initializer): InitializerScope {
            return getScope(data, initializer) ?: DEFAULT
        }


    }

    fun requireScope(initializer: Initializer): InitializerScope {
        return requireScope(data, initializer)
    }

    internal val owner: InitializerScopeOwner = InitializerScopeOwner()

    internal fun setup(initializer: Initializer) {
        lock.withLock {
            if (scopeMap.containsKey(initializer)) return@withLock
            InitializerScopeImpl(this).run {
                scopeMap[initializer] = this
                owner.addScope(this)
                initializer.setup()
            }
        }
    }

    internal fun setup(initializers: LockList<Initializer>) {
        initializers.forEach {
            setup(this)
        }
    }

    internal fun destroy() {
        owner.destroy()
        remove(data)
        lock.withLock {
            scopeMap.clear()
        }
    }

    class FragmentTarget internal constructor(
        override val data: Fragment,
    ) : FeatureTarget(data) {
        internal lateinit var fragmentManager: FragmentManager
        internal lateinit var context: Context

        internal fun hasFragmentManager(): Boolean {
            return ::fragmentManager.isInitialized
        }

        internal fun hasContext(): Boolean {
            return ::context.isInitialized
        }
        internal fun doOnFragmentLifecycle(action:FragmentManager.FragmentLifecycleCallbacks.()->Unit){
          owner.doOnFragmentLifecycle(action)
        }
    }

    class ActivityTarget internal constructor(
        override val data: Activity,
    ) : FeatureTarget(data) {
        internal var savedInstanceState: Bundle? = null
        internal fun doOnActivityLifecycle(action: Application.ActivityLifecycleCallbacks.() -> Unit) {
            owner.doOnActivityLifecycle(action)
        }
    }

    class ApplicationTarget internal constructor(
        override val data: Application,
    ) : FeatureTarget(data)
}
