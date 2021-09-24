package com.x930073498.features.internal

import android.content.Context
import com.x930073498.features.core.*
import com.x930073498.lifecycle.core.InitializerScope
import com.x930073498.lifecycle.core.Removable

internal class FeatureHandlerStoreImpl : FeatureHandlerStore() {
    private val handlers = arrayListOf<FeatureHandler<*, *>>()

    fun handle(scope: InitializerScope, component: Any,context: Context) {
        handlers.forEach {
            it.handle(scope, component,context)
        }
    }

    override fun <C, F : IFeature> addHandler(
        clazz: Class<C>,
        parser: FeatureParser<C, F>,
        action: FeatureAction<C, F>
    ) = run {
        val handler = FeatureHandler(clazz, parser, action)
        handlers.add(handler)
        object : Removable {
            override fun remove() {
                handlers.remove(handler)
            }

        }
    }
}