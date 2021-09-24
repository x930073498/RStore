package com.x930073498.features.internal

import com.x930073498.features.core.*
import com.x930073498.lifecycle.core.InitializerScope

internal class FeatureScopeImpl(
    initializerScope: InitializerScope,
    private val store: FeatureHandlerStore
) :
    FeatureScope, InitializerScope by initializerScope, FeatureHandlerStore() {
    override fun <C, F : IFeature> addHandler(
        clazz: Class<C>,
        parser: FeatureParser<C, F>,
        action: FeatureAction<C, F>
    ) = store.addHandler(clazz, parser, action)


}