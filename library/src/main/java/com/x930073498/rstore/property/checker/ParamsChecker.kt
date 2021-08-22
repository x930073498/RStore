package com.x930073498.rstore.property.checker

import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.property.DelegateProcess
import com.x930073498.rstore.property.StateChecker
import kotlin.reflect.KProperty

internal class ParamsChecker<T : IStoreProvider, Data, Source>(
    private val shouldSaveState: Boolean,
    private val isAnchorProperty: Boolean
) : StateChecker<T, Data, Source> {
    override fun T.shouldSaveState(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, Source>
    ): Boolean {
        return shouldSaveState
    }

    override fun T.isAnchorProperty(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, Source>
    ): Boolean {
        return isAnchorProperty
    }
}