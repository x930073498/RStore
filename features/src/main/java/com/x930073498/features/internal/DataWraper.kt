package com.x930073498.features.internal

import com.x930073498.features.core.Feature
import com.x930073498.features.core.FeatureInstaller
import com.x930073498.features.core.FeatureLifecycle


internal open class FeatureWrapper<T : Feature>(
    private val feature: T,
    installer: FeatureInstaller<T>
) : FeatureTargetWrapper<T>(feature.javaClass, installer) {
    override fun getFeature(target: Any): T? {
        return feature
    }
}

internal open class FeatureTargetWrapper<T : Feature>(
    private val featureClass: Class<T>,
    private val installer: FeatureInstaller<T>
) {
    fun install(target: Any, lifecycle: FeatureLifecycle) {
        getFeature(target)?.run {
            installer.onInstall(this, lifecycle)
        }
    }

    protected open fun getFeature(target: Any): T? {
        return if (featureClass.isInstance(target)) {
            featureClass.cast(target)
        } else null
    }
}

internal class TargetClassDataWrapper<T : Feature, V>(
    private val clazz: Class<V>,
    private val feature: T,
    installer: FeatureInstaller<T>
) : FeatureTargetWrapper<T>(feature.javaClass, installer) {

    override fun getFeature(target: Any): T? {
        return if (clazz.isInstance(target)) feature else null
    }

}