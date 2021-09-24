package com.x930073498.features.internal

import com.x930073498.features.core.FeatureAction
import com.x930073498.features.core.FeatureParser
import com.x930073498.features.core.IFeature
import com.x930073498.features.core.IFeatureHandlerStore
import com.x930073498.lifecycle.core.Removable


abstract class FeatureHandlerStore : IFeatureHandlerStore {

    internal abstract fun <C, F : IFeature> addHandler(
        clazz: Class<C>,
        parser: FeatureParser<C, F>,
        action: FeatureAction<C, F>
    ): Removable

}

@PublishedApi
internal fun <C, F : IFeature> IFeatureHandlerStore.addHandler(
    clazz: Class<C>,
    parser: FeatureParser<C, F>,
    action: FeatureAction<C, F>
): Removable =
    if (this is FeatureHandlerStore) addHandler(clazz, parser, action) else Removable
