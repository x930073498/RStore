package com.x930073498.rstore.property

import com.x930073498.rstore.core.Feature
import com.x930073498.rstore.core.Feature.Companion.hasFeature
import com.x930073498.rstore.core.IStoreProvider
import kotlin.reflect.KProperty

interface FeatureProvider{
    val feature: Feature
        get() {
            return Feature
        }
}

internal fun FeatureProvider(feature: Feature)=object :FeatureProvider{
    override val feature: Feature=feature
}
