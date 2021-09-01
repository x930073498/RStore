package com.x930073498.rstore.anchor

import com.x930073498.rstore.core.*
import com.x930073498.rstore.core.PropertyChanged
import com.x930073498.rstore.internal.*
import com.x930073498.rstore.internal.hasFeature
import com.x930073498.rstore.internal.invokeAction
import com.x930073498.rstore.internal.removeFeature
import com.x930073498.rstore.internal.valueFromProperty
import kotlinx.coroutines.withContext
import kotlin.reflect.KProperty

internal class PropertyAction<T : IStoreProvider, V>(
    private val property: KProperty<V>,
    private val action: V.() -> Unit,
) : AnchorStateEventAction<T> {
    override suspend fun T.process(data: AnchorScopeState) {
        removeFeature(property,AnchorUnregister)
        valueFromProperty(property)
        if (!hasFeature(property, Feature.Anchor)) return
        with(data) {
            val shouldRun = !isInitialized || hasFeature(property, PropertyChanged)
            if (shouldRun) {
                withContext(main) {
                    invokeAction(property, action)
                }
                removeFeature(property, PropertyChanged)
            }
        }

    }


}