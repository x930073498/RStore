package com.x930073498.features.internal


import android.content.Context
import com.x930073498.features.core.FeatureAction
import com.x930073498.features.core.FeatureData
import com.x930073498.features.core.FeatureParser
import com.x930073498.features.core.IFeature
import com.x930073498.lifecycle.core.InitializerScope

internal class FeatureHandler<C, F : IFeature> constructor(
    private val clazz: Class<C>,
    private val parser: FeatureParser<C, F>,
    private val action: FeatureAction<C, F>
) {
    fun handle(scope: InitializerScope, component: Any,context: Context) {
        if (clazz.isInstance(component)) {
            val c = clazz.cast(component) ?: return
            handleActual(scope, c,context)
        }

    }

    private fun handleActual(scope: InitializerScope, component: C,context: Context) {
        with(action) {
            val feature = parser.parse(component)
            if (feature != null) {
                val data = FeatureData(component,context, feature)
                data.action(scope)
            }
        }
    }

}