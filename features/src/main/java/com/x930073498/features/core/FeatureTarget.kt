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
import com.x930073498.features.internal.forEach
import com.x930073498.features.internal.fragment.FragmentFeatureLifecycleImpl

sealed class FeatureTarget(open val data: Any) {
    abstract val featureLifecycle: FeatureLifecycle

    internal fun setup(initializer: Initializer) {
        initializer.init(this@FeatureTarget)
    }

    internal fun setup(initializers: LockList<Initializer>) {
        initializers.forEach {
            init(this@FeatureTarget)
        }
    }

    class FragmentTarget internal constructor(
        override val data: Fragment,
    ) :
        FeatureTarget(data) {
        private val _featureLifecycle = FragmentFeatureLifecycleImpl()

        override val featureLifecycle: FragmentFeatureLifecycle
            get() = _featureLifecycle


    }

    class ActivityTarget internal constructor(
        override val data: Activity,
    ) : FeatureTarget(data) {
        private val _featureLifecycle = ActivityFeatureLifecycleImpl()


        override val featureLifecycle: ActivityFeatureLifecycle
            get() = _featureLifecycle


        override fun toString(): String {
            return "ActivityTarget(data=$data, _featureLifecycle=$_featureLifecycle, featureLifecycle=$featureLifecycle)"
        }


    }

    class ApplicationTarget internal constructor(
        override val data: Application,
    ) : FeatureTarget(data) {
        private val _featureLifecycle = ApplicationFeatureLifecycleImpl()


        override val featureLifecycle: ApplicationFeatureLifecycle
            get() = _featureLifecycle

    }
}
