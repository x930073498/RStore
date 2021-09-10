package com.x930073498.rstore.anchor

import com.x930073498.rstore.core.AnchorScopeState
import com.x930073498.rstore.core.AnchorStateEventAction
import com.x930073498.rstore.core.Feature
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.internal.*
import com.x930073498.rstore.internal.hasFeature
import com.x930073498.rstore.internal.valueFromProperty
import com.x930073498.rstore.property.equals.WrapEquals
import com.x930073498.rstore.property.equals.WrapEquals.Companion.equalsData
import kotlinx.coroutines.withContext
import kotlin.reflect.KProperty

internal class PropertyAction<T : IStoreProvider, V>(
    private val property: KProperty<V>,
    private val action: V.() -> Unit,
) : AnchorStateEventAction<T, V> {
    override suspend fun T.process(data: AnchorScopeState): V {
        var value = valueFromProperty(property)
        if (!hasFeature(property, Feature.Anchor)) return value
//        val equals = getEqualsImpl(property) as WrapEquals<V, Any?>
        with(data) {
//            val isEqualPre = equals.equalsData(value, valueHolder.getPropertyValue(property.name))
            val isEqualPre = false
            val shouldRun =
                !isInitialized || (stateHolder.isPropertyChanged(property.name) && !isEqualPre)
            if (shouldRun) {
                withContext(main) {
                    value = valueFromProperty(property)
                    action(value)
                }
            }
            stateHolder.setPropertyState(property.name, false)
//            valueHolder.setPropertyValue(property.name, equals.transform(value))
        }
        return value
    }


}