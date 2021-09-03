package com.x930073498.features.internal

import com.x930073498.features.core.Feature
import com.x930073498.features.core.FeatureInstaller
import com.x930073498.features.core.FeatureLifecycle
import com.x930073498.features.core.application.ApplicationFeatureInstaller
import java.util.concurrent.locks.ReentrantLock

@PublishedApi
internal object FeatureRepository {
    private val wrapperList = arrayListOf<FeatureTargetWrapper<*>>()
    private val lock = ReentrantLock()
    private fun <R> doOnLock(action: MutableList<FeatureTargetWrapper<*>>.() -> R): R {
        lock.lock()
        val result = action(wrapperList)
        lock.unlock()
        return result
    }

    fun runFeatureInstall(component: Any, lifecycle: FeatureLifecycle) {
        doOnLock {
            forEach {
                it.install(component, lifecycle)
            }
        }

    }

    fun <T : Feature> addFeatureInstaller(feature: Class<T>, installer: FeatureInstaller<T>) {
        doOnLock {
            add(FeatureTargetWrapper(feature, installer))
        }
    }

    fun <T : Feature> addFeatureInstaller(feature: T, installer: ApplicationFeatureInstaller<T>) {
        doOnLock {
            add(FeatureWrapper(feature, installer))
        }
    }

    fun <T : Feature, V> addFeatureInstaller(
        clazz: Class<V>,
        feature: T,
        installer: FeatureInstaller<T>
    ) {
        doOnLock {
            add(TargetClassDataWrapper(clazz, feature, installer))
        }
    }
}