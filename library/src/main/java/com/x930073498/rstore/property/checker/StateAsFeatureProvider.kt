package com.x930073498.rstore.property.checker

import com.x930073498.rstore.core.Feature
import com.x930073498.rstore.core.Feature.Companion.plus
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.FeatureProvider
import com.x930073498.rstore.property.lazyField
import kotlin.reflect.KProperty

internal class StateAsFeatureProvider(
    private val shouldSaveState: Boolean,
    private val isAnchorProperty: Boolean
) : FeatureProvider {
    override val feature: Feature by lazyField {
        var feature: Feature = Feature
        if (shouldSaveState) feature += Feature.SaveState
        if (isAnchorProperty) feature += Feature.Anchor
        feature
    }
}