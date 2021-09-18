@file:Suppress("LocalVariableName")

package com.x930073498.features.initializer

import com.x930073498.features.core.IFeature
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.Initializer
import com.x930073498.features.core.InitializerScope

open class TargetData<T, F : IFeature> internal constructor(
    scope: InitializerScope,
    initializer: Initializer,
    open val data: T,
    open val feature: F,
    open val target: FeatureTarget
) : InitializerScope by scope,Initializer by initializer {
    override fun toString(): String {
        return "TargetData(data=$data, feature=$feature, target=$target)"
    }


}


class ApplicationTargetData<T, F : IFeature> internal constructor(
    scope: InitializerScope,
    initializer: Initializer,
    override val data: T,
    override val feature: F,
    override val target: FeatureTarget.ApplicationTarget
) : TargetData<T, F>(scope,initializer, data, feature, target)

class ActivityTargetData<T, F : IFeature> internal constructor(
    scope: InitializerScope,
    initializer: Initializer,
    override val data: T,
    override val feature: F,
    override val target: FeatureTarget.ActivityTarget
) : TargetData<T, F>(scope,initializer,data, feature, target)

class FragmentTargetData<T, F : IFeature> internal constructor(
    scope: InitializerScope,
    initializer: Initializer,
    override val data: T,
    override val feature: F,
    override val target: FeatureTarget.FragmentTarget
) : TargetData<T, F>(scope,initializer,data, feature, target)


@PublishedApi
internal class TargetClassInitializer<T, F : IFeature>(
    private val targetClass: Class<T>,
    private val feature: F,
    private val action: TargetData<T, F>.() -> Unit
) : Initializer {
    override fun InitializerScope.setup(target: FeatureTarget) {
        feature.onFeatureInitialized(target)
        val data = target.data
        if (targetClass.isInstance(data)) {
            val targetData = when (target) {
                is FeatureTarget.ActivityTarget -> ActivityTargetData(
                    this,
                    this@TargetClassInitializer,
                    targetClass.cast(data) as T,
                    feature,
                    target
                )
                is FeatureTarget.ApplicationTarget -> ApplicationTargetData(
                    this,
                    this@TargetClassInitializer,
                    targetClass.cast(data) as T,
                    feature,
                    target
                )
                is FeatureTarget.FragmentTarget -> FragmentTargetData(
                    this,
                    this@TargetClassInitializer,
                    targetClass.cast(data) as T,
                    feature,
                    target
                )
            }
            action(targetData)
        }
    }
}