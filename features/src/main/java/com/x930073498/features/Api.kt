package com.x930073498.features

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.x930073498.features.core.Feature
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.Initializer
import com.x930073498.features.extentions.*
import com.x930073498.features.extentions.InstanceFeatureInitializer
import com.x930073498.features.extentions.TargetClassInitializer
import com.x930073498.features.internal.Features


fun addInitializer(initializer: Initializer) {
    Features.addInitializer(initializer)
}

inline fun <reified F : Feature> installInstanceFeature(noinline action: F.(FeatureTarget) -> Unit) {
    addInitializer(InstanceFeatureInitializer(F::class.java, action))
}

fun <T : Activity, F : Feature> installActivityFeature(
    clazz: Class<T>,
    feature: F,
    action: ActivityTargetData<T, F>.() -> Unit
) {
    addInitializer(TargetClassInitializer(clazz, feature) {
        action(this as ActivityTargetData<T, F>)
    })
}

fun <T : Fragment, F : Feature> installFragmentFeature(
    clazz: Class<T>,
    feature: F,
    action: FragmentTargetData<T, F>.() -> Unit
) {
    addInitializer(TargetClassInitializer(clazz, feature){
        action(this as FragmentTargetData<T, F>)
    })
}

fun <F : Feature> installApplicationFeature(
    feature: F,
    action: ApplicationTargetData<Application, F>.() -> Unit
) {
    addInitializer(TargetClassInitializer(Application::class.java, feature){
        action(this as ApplicationTargetData<Application, F>)
    })
}


