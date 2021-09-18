package com.x930073498.features

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.startup.AppInitializer
import com.x930073498.features.core.*
import com.x930073498.features.initializer.*
import com.x930073498.features.internal.ActivityLifecycleOwner
import com.x930073498.features.internal.Features


val application by ::app

val Activity.lifecycleOwner: LifecycleOwner
    get() = if (this is LifecycleOwner) this else ActivityLifecycleOwner.require(this)


val LifecycleOwner.activity: Activity?
    get() {
        return if (this is Activity) this else if (this is ActivityLifecycleOwner) activity else null
    }

fun Initializer.remove(){
    Features.remove(this)
}

fun forceInitFeatureComponent(context: Context) =
    AppInitializer.getInstance(context).initializeComponent(FeatureStartup::class.java)


fun addInitializer(initializer: Initializer) =
    Features.addInitializer(initializer)


fun addLifecycleObserver(
    applicationLifecycleObserver: LifecycleObserver = DefaultObserver,
    activityLifecycleObserver: LifecycleObserver = DefaultObserver,
    fragmentLifecycleObserver: LifecycleObserver = DefaultObserver,
    fragmentViewLifecycleObserver: LifecycleObserver = DefaultObserver
) =
    addInitializer(
        LifecycleInitializer(
            applicationLifecycleObserver,
            activityLifecycleObserver,
            fragmentLifecycleObserver,
            fragmentViewLifecycleObserver
        )
    )


fun addLifecycleObserver(
    applicationFeatureLifecycleObserver: ApplicationFeatureLifecycleObserver = ApplicationFeatureLifecycleObserver,
    activityFeatureLifecycleObserver: ActivityFeatureLifecycleObserver = ActivityFeatureLifecycleObserver,
    fragmentFeatureLifecycleObserver: FragmentFeatureLifecycleObserver = FragmentFeatureLifecycleObserver
) = addInitializer(
    ObserverInitializer(
        applicationFeatureLifecycleObserver,
        activityFeatureLifecycleObserver,
        fragmentFeatureLifecycleObserver
    )
)

fun addActivityLifecycleObserver(observer: ActivityFeatureLifecycleObserver) =
    addInitializer(ObserverInitializer(activityFeatureLifecycleObserver = observer))

fun addActivityLifecycleObserver(observer: LifecycleObserver) =
    addLifecycleObserver(activityLifecycleObserver = observer)


@RequiresApi(29)
fun addActivityLifecycleObserver(observer: ActivityFeatureStateObserver) =
    addInitializer(ObserverInitializer(activityFeatureLifecycleObserver = observer))


fun addFragmentLifecycleObserver(observer: FragmentFeatureLifecycleObserver) =
    addInitializer(ObserverInitializer(fragmentFeatureLifecycleObserver = observer))

fun addFragmentLifecycleObserver(observer: LifecycleObserver, bindView: Boolean = true) =
    if (bindView) addLifecycleObserver(fragmentViewLifecycleObserver = observer) else addLifecycleObserver(
        fragmentLifecycleObserver = observer
    )


fun addApplicationLifecycleObserver(observer: ApplicationFeatureLifecycleObserver) =
    addInitializer(ObserverInitializer(applicationFeatureLifecycleObserver = observer))

fun addApplicationLifecycleObserver(observer: LifecycleObserver) =
    addLifecycleObserver(applicationLifecycleObserver = observer)

inline fun <reified F : IFeature> installInstanceFeature(noinline action: TargetData<Any, F>.() -> Unit) =
    addInitializer(InstanceFeatureInitializer(F::class.java, action))


fun <T : Activity, F : IFeature> installActivityFeature(
    clazz: Class<T>,
    feature: F,
    action: ActivityTargetData<T, F>.() -> Unit
) = addInitializer(TargetClassInitializer(clazz, feature) {
    action(this as ActivityTargetData<T, F>)
})


fun <T : Fragment, F : IFeature> installFragmentFeature(
    clazz: Class<T>,
    feature: F,
    action: FragmentTargetData<T, F>.() -> Unit
) = addInitializer(
    TargetClassInitializer(
        clazz,
        feature
    ) { action(this as FragmentTargetData<T, F>) })


fun <F : IFeature> installApplicationFeature(
    feature: F,
    action: ApplicationTargetData<Application, F>.() -> Unit
) = addInitializer(
    TargetClassInitializer(
        Application::class.java,
        feature
    ) { action(this as ApplicationTargetData<Application, F>) })



