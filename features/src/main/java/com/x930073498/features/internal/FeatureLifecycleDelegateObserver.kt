package com.x930073498.features.internal

import com.x930073498.features.core.FeatureLifecycleObserver
import com.x930073498.features.core.activity.ActivityFeatureLifecycleObserver
import com.x930073498.features.core.application.ApplicationFeatureLifecycleObserver
import com.x930073498.features.core.fragment.FragmentFeatureLifecycleObserver

internal class FeatureLifecycleDelegateObserver(
    val fragmentFeatureLifecycleObserver: FragmentFeatureLifecycleObserver = FragmentFeatureLifecycleObserver,
    val activityFeatureLifecycleObserver: ActivityFeatureLifecycleObserver = ActivityFeatureLifecycleObserver,
    val applicationFeatureLifecycleObserver: ApplicationFeatureLifecycleObserver = ApplicationFeatureLifecycleObserver
) :
    FeatureLifecycleObserver, FragmentFeatureLifecycleObserver by fragmentFeatureLifecycleObserver,
    ActivityFeatureLifecycleObserver by activityFeatureLifecycleObserver,
    ApplicationFeatureLifecycleObserver by applicationFeatureLifecycleObserver {
    constructor(observer: FeatureLifecycleObserver) : this(observer, observer, observer)

    val isFragmentFeature = fragmentFeatureLifecycleObserver != FragmentFeatureLifecycleObserver
    val isActivityFeature = activityFeatureLifecycleObserver != ActivityFeatureLifecycleObserver
    val isApplicationFeature =
        applicationFeatureLifecycleObserver != ApplicationFeatureLifecycleObserver

}