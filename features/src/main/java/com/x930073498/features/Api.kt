package com.x930073498.features

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.startup.AppInitializer
import com.x930073498.features.core.IFeature
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.Initializer
import com.x930073498.features.core.activity.ActivityFeatureLifecycleObserver
import com.x930073498.features.core.activity.ActivityFeatureStateObserver
import com.x930073498.features.core.application.ApplicationFeatureLifecycleObserver
import com.x930073498.features.core.fragment.FragmentFeatureLifecycleObserver
import com.x930073498.features.extentions.*
import com.x930073498.features.extentions.InstanceFeatureInitializer
import com.x930073498.features.extentions.TargetClassInitializer
import com.x930073498.features.internal.Features


val application by ::app


fun requireFeatureInit(context: Context) {
    AppInitializer.getInstance(context).initializeComponent(FeatureStartup::class.java)
}

fun addInitializer(initializer: Initializer) {
    Features.addInitializer(initializer)
}

fun addLifecycleObserver(
    applicationFeatureLifecycleObserver: ApplicationFeatureLifecycleObserver = ApplicationFeatureLifecycleObserver,
    activityFeatureLifecycleObserver: ActivityFeatureLifecycleObserver = ActivityFeatureLifecycleObserver,
    fragmentFeatureLifecycleObserver: FragmentFeatureLifecycleObserver = FragmentFeatureLifecycleObserver
) = addInitializer(
    LifecycleInitializer(
        applicationFeatureLifecycleObserver,
        activityFeatureLifecycleObserver,
        fragmentFeatureLifecycleObserver
    )
)

fun addActivityLifecycleObserver(observer: ActivityFeatureLifecycleObserver) =
    addInitializer(LifecycleInitializer(activityFeatureLifecycleObserver = observer))

@RequiresApi(29)
fun addActivityLifecycleObserver(observer: ActivityFeatureStateObserver) =
    addInitializer(LifecycleInitializer(activityFeatureLifecycleObserver = observer))


fun addFragmentLifecycleObserver(observer: FragmentFeatureLifecycleObserver) =
    addInitializer(LifecycleInitializer(fragmentFeatureLifecycleObserver = observer))


fun addApplicationLifecycleObserver(observer: ApplicationFeatureLifecycleObserver) =
    addInitializer(LifecycleInitializer(applicationFeatureLifecycleObserver = observer))

inline fun <reified F : IFeature> installInstanceFeature(noinline action: F.(FeatureTarget) -> Unit) {
    addInitializer(InstanceFeatureInitializer(F::class.java, action))
}

fun <T : Activity, F : IFeature> installActivityFeature(
    clazz: Class<T>,
    feature: F,
    action: ActivityTargetData<T, F>.() -> Unit
) {
    addInitializer(TargetClassInitializer(clazz, feature) {
        action(this as ActivityTargetData<T, F>)
    })
}

fun <T : Fragment, F : IFeature> installFragmentFeature(
    clazz: Class<T>,
    feature: F,
    action: FragmentTargetData<T, F>.() -> Unit
) {
    addInitializer(
        TargetClassInitializer(
            clazz,
            feature
        ) { action(this as FragmentTargetData<T, F>) })
}

fun <F : IFeature> installApplicationFeature(
    feature: F,
    action: ApplicationTargetData<Application, F>.() -> Unit
) {
    addInitializer(
        TargetClassInitializer(
            Application::class.java,
            feature
        ) { action(this as ApplicationTargetData<Application, F>) })
}


