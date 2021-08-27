package com.x930073498.rstore.anchor

import com.x930073498.rstore.core.AnchorScopeState
import com.x930073498.rstore.core.AnchorStateEventAction
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.internal.invokeAction
import com.x930073498.rstore.internal.valueFromProperty
import kotlinx.coroutines.withContext
import kotlin.reflect.KProperty

internal class PropertyAction<T : IStoreProvider, V>(
    private val property: KProperty<V>,
    private val action: V.() -> Unit,
) : AnchorStateEventAction<T> {
    override suspend fun T.process(data: AnchorScopeState) {
        valueFromProperty(property)
        with(data) {
            val delegateProperty = container.getDelegateProperty(property)
            if (delegateProperty != null) {
                withContext(main) {
                    invokeAction(property, action)
                }
                container.removeDelegateProperty(delegateProperty)
            }
        }

    }


}