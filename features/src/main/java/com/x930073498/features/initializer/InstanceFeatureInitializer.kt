package com.x930073498.features.initializer

import com.x930073498.features.core.IFeature
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.Initializer
import com.x930073498.features.core.InitializerScope

@PublishedApi
internal class InstanceFeatureInitializer<T : IFeature>(
    private val featureClass: Class<T>,
    private val action: TargetData<Any, T>.() -> Unit
) : Initializer {
    override fun InitializerScope.setup(target: FeatureTarget) {
        val data = target.data
        if (featureClass.isInstance(data)) {
            val feature = featureClass.cast(data) as T
            feature.onFeatureInitialized(target)
            action(TargetData(this,this@InstanceFeatureInitializer, data, feature, target))
        }
    }
}