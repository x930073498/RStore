package com.x930073498.rstore.core

import com.x930073498.rstore.internal.propertyFeatureImpl

interface IStoreProviderComponent : IStoreProvider, ISaveStateStoreProvider {
    fun <T> T.asProperty(feature: Feature=Feature.All, equals: Equals<T> = DefaultEquals()) =
        propertyFeatureImpl(this@IStoreProviderComponent,equals,feature)


}








