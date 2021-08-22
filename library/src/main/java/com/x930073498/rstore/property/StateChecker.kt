package com.x930073498.rstore.property

import com.x930073498.rstore.core.IStoreProvider
import kotlin.reflect.KProperty

interface StateChecker<T : IStoreProvider, Data, Source> {
    fun T.shouldSaveState(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, Source>,
    ): Boolean


    fun T.isAnchorProperty(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, Source>
    ): Boolean
}