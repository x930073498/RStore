package com.x930073498.rstore.property.factory

import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.internal.registerPropertyChangedListenerImpl
import com.x930073498.rstore.internal.valueFromProperty
import com.x930073498.rstore.property.SourceFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.stateIn
import kotlin.reflect.KProperty

internal class TargetFlowPropertyFactory<T : IStoreProvider, Data, Target>(
    private val provider: T,
    private val targetProperty: KProperty<Data>,
    private val transfer: Data.() -> Target,
) :
    SourceFactory<Target, MutableStateFlow<Target>> {
    override fun createSource(data: Target?): MutableStateFlow<Target> {
        return with(provider) {
            val value = valueFromProperty(targetProperty)
            MutableStateFlow(transfer(value)).also {
                registerPropertyChangedListenerImpl(targetProperty) {
                    it.tryEmit(transfer(this))
                }
            }
        }
    }


}