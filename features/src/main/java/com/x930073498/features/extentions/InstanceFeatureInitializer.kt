package com.x930073498.features.extentions

import com.x930073498.features.core.IFeature
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.Initializer

@PublishedApi
internal class InstanceFeatureInitializer<T : IFeature>(
    private val featureClass: Class<T>,
    private val action: T.(FeatureTarget) -> Unit
) : Initializer {
    override fun init(target: FeatureTarget) {
        val data = target.data
        if (featureClass.isInstance(data)) {
            val feature = featureClass.cast(data) as T
            feature.onFeatureInitialized(target)
            action(feature, target)
        }
    }
}