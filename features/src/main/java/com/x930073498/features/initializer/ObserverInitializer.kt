package com.x930073498.features.initializer

import android.os.Build
import com.x930073498.features.core.*

internal class ObserverInitializer constructor(
    val applicationFeatureLifecycleObserver: ApplicationFeatureLifecycleObserver = ApplicationFeatureLifecycleObserver,
    val activityFeatureLifecycleObserver: ActivityFeatureLifecycleObserver = ActivityFeatureLifecycleObserver,
    val fragmentFeatureLifecycleObserver: FragmentFeatureLifecycleObserver = FragmentFeatureLifecycleObserver
) : Initializer {
    override fun InitializerScope.setup(target: FeatureTarget) {
        when (target) {
            is FeatureTarget.ActivityTarget -> {
                if (activityFeatureLifecycleObserver === ActivityFeatureLifecycleObserver) return
                if (activityFeatureLifecycleObserver is ActivityFeatureStateObserver) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        target.addObserver(activityFeatureLifecycleObserver)
                    } else {
                        target.addObserver(activityFeatureLifecycleObserver as ActivityFeatureLifecycleObserver)
                    }
                } else {
                    target.addObserver(activityFeatureLifecycleObserver)
                }
            }
            is FeatureTarget.ApplicationTarget -> {
                if (applicationFeatureLifecycleObserver === ApplicationFeatureLifecycleObserver) return
                target.addObserver(applicationFeatureLifecycleObserver)
            }
            is FeatureTarget.FragmentTarget -> {
                if (fragmentFeatureLifecycleObserver === FragmentFeatureLifecycleObserver) return
                target.addObserver(fragmentFeatureLifecycleObserver)
            }
        }
    }
}