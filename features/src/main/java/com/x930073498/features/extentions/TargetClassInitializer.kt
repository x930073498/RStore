@file:Suppress("LocalVariableName")

package com.x930073498.features.extentions

import com.x930073498.features.core.Feature
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.Initializer

open class TargetData<T, F : Feature>(
    open val data: T,
    open val feature: F,
    open val target: FeatureTarget
) {
    override fun toString(): String {
        return "TargetData(data=$data, feature=$feature, target=$target)"
    }
}


class ApplicationTargetData<T, F : Feature>(
    override val data: T,
    override val feature: F,
    override val target: FeatureTarget.ApplicationTarget
) : TargetData<T, F>(data, feature, target)

class ActivityTargetData<T, F : Feature>(
    override val data: T,
    override val feature: F,
    override val target: FeatureTarget.ActivityTarget
) : TargetData<T, F>(data, feature, target)

class FragmentTargetData<T, F : Feature>(
    override val data: T,
    override val feature: F,
    override val target: FeatureTarget.FragmentTarget
) : TargetData<T, F>(data, feature, target)


@PublishedApi
internal class TargetClassInitializer<T, F : Feature>(
    private val targetClass: Class<T>,
    private val feature: F,
    private val action: TargetData<T, F>.() -> Unit
) : Initializer {
    override fun init(target: FeatureTarget) {
        val data = target.data
        if (targetClass.isInstance(data)) {
            val targetData = when (target) {
                is FeatureTarget.ActivityTarget -> ActivityTargetData(
                    targetClass.cast(data) as T,
                    feature,
                    target
                )
                is FeatureTarget.ApplicationTarget -> ApplicationTargetData(
                    targetClass.cast(data) as T,
                    feature,
                    target
                )
                is FeatureTarget.FragmentTarget -> FragmentTargetData(
                    targetClass.cast(data) as T,
                    feature,
                    target
                )
            }
            action(targetData)
        }
    }
}