package com.x930073498.rstore.core

import com.x930073498.rstore.internal.propertyFeatureImpl

interface IStoreProviderComponent : IStoreProvider, ISaveStateStoreProvider {
    fun <T> T.propertyFeature(vararg feature: Feature) =
        propertyFeatureImpl(this@IStoreProviderComponent,*feature)


}








