package com.x930073498.rstore.anchor

import com.x930073498.rstore.core.AnchorScopeState
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.event.EventAction
import com.x930073498.rstore.internal.invokeAction
import com.x930073498.rstore.internal.valueFromProperty
import kotlin.reflect.KProperty

internal class PropertyAction<T : IStoreProvider, V>(
    private val property: KProperty<V>,
    private val action: V.() -> Unit,
) : EventAction<T, AnchorScopeState> {
    private var delegateProperty: KProperty<*>? = null

    override fun T.init(data: AnchorScopeState) {
        valueFromProperty(property)
    }

    override fun T.enable(data: AnchorScopeState): Boolean {
        return with(data) {
            delegateProperty = container.getDelegateProperty(property)
            delegateProperty != null
        }
    }

    override suspend fun T.process(data: AnchorScopeState) {
        with(data) {
            invokeAction(property, action)
            delegateProperty?.apply {
                container.removeDelegateProperty(this)
            }
        }
    }

}