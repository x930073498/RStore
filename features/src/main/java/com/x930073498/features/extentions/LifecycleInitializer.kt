package com.x930073498.features.extentions

import android.os.Build
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.Initializer
import com.x930073498.features.core.activity.ActivityFeatureLifecycleObserver
import com.x930073498.features.core.activity.ActivityFeatureStateObserver
import com.x930073498.features.core.application.ApplicationFeatureLifecycleObserver
import com.x930073498.features.core.fragment.FragmentFeatureLifecycleObserver

internal class LifecycleInitializer constructor(
    val applicationFeatureLifecycleObserver: ApplicationFeatureLifecycleObserver = ApplicationFeatureLifecycleObserver,
    val activityFeatureLifecycleObserver: ActivityFeatureLifecycleObserver = ActivityFeatureLifecycleObserver,
    val fragmentFeatureLifecycleObserver: FragmentFeatureLifecycleObserver = FragmentFeatureLifecycleObserver
) : Initializer {
    override fun init(target: FeatureTarget) {
        when (target) {
            is FeatureTarget.ActivityTarget -> {
                if (activityFeatureLifecycleObserver === ActivityFeatureLifecycleObserver) return
                if (activityFeatureLifecycleObserver is ActivityFeatureStateObserver) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        target.featureLifecycle.addObserver(activityFeatureLifecycleObserver)
                    } else {
                        target.featureLifecycle.addObserver(activityFeatureLifecycleObserver as ActivityFeatureLifecycleObserver)
                    }
                } else {
                    target.featureLifecycle.addObserver(activityFeatureLifecycleObserver)
                }
            }
            is FeatureTarget.ApplicationTarget -> {
                if (applicationFeatureLifecycleObserver === ApplicationFeatureLifecycleObserver) return
                target.featureLifecycle.addObserver(applicationFeatureLifecycleObserver)
            }
            is FeatureTarget.FragmentTarget -> {
                if (fragmentFeatureLifecycleObserver === FragmentFeatureLifecycleObserver) return
                target.featureLifecycle.addObserver(fragmentFeatureLifecycleObserver)
            }
        }
    }
}