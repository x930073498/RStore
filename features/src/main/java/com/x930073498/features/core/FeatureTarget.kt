package com.x930073498.features.core

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.x930073498.features.core.activity.ActivityFeatureLifecycle
import com.x930073498.features.core.application.ApplicationFeatureLifecycle
import com.x930073498.features.core.fragment.FragmentFeatureLifecycle
import com.x930073498.features.internal.LockList
import com.x930073498.features.internal.activity.ActivityFeatureLifecycleImpl
import com.x930073498.features.internal.application.ApplicationFeatureLifecycleImpl
import com.x930073498.features.internal.doOnLock
import com.x930073498.features.internal.fragment.FragmentFeatureLifecycleImpl

sealed class FeatureTarget(open val data: Any) {
    abstract val featureLifecycle: FeatureLifecycle

    internal fun setup(initializer: Initializer) {
            initializer.init(this@FeatureTarget)
    }

    class FragmentTarget(
        override val data: Fragment,
        private val initializers: LockList<Initializer>
    ) :
        FeatureTarget(data) {
        override val featureLifecycle: FragmentFeatureLifecycle =
            FragmentFeatureLifecycleImpl(this, initializers)


    }

    class ActivityTarget(
        override val data: Activity,
        initializers: LockList<Initializer>
    ) : FeatureTarget(data) {
        override val featureLifecycle: ActivityFeatureLifecycle =
            ActivityFeatureLifecycleImpl(this, initializers)
    }

    class ApplicationTarget(
        override val data: Application,
        initializers: LockList<Initializer>
    ) : FeatureTarget(data) {
        override val featureLifecycle: ApplicationFeatureLifecycle =
            ApplicationFeatureLifecycleImpl(this, initializers)
    }
}
