package com.x930073498.features.core

import com.x930073498.features.core.activity.ActivityFeatureLifecycleObserver
import com.x930073498.features.core.application.ApplicationFeatureLifecycleObserver
import com.x930073498.features.core.fragment.FragmentFeatureLifecycleObserver

interface FeatureLifecycleObserver : FragmentFeatureLifecycleObserver, ActivityFeatureLifecycleObserver,ApplicationFeatureLifecycleObserver