package com.x930073498.features.initializer

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ProcessLifecycleOwner
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.Initializer
import com.x930073498.features.core.InitializerScope
import com.x930073498.features.lifecycleOwner


internal object DefaultObserver : LifecycleObserver
class LifecycleInitializer(
    private val applicationLifecycleObserver: LifecycleObserver = DefaultObserver,
    private val activityLifecycleObserver: LifecycleObserver = DefaultObserver,
    private val fragmentLifecycleObserver: LifecycleObserver = DefaultObserver,
    private val fragmentViewLifecycleObserver: LifecycleObserver = DefaultObserver,
) : Initializer {
    override fun InitializerScope.setup(target: FeatureTarget) {
        with(target) {
            when (this) {
                is FeatureTarget.ActivityTarget -> {
                    if (activityLifecycleObserver !== DefaultObserver)
                        data.lifecycleOwner.lifecycle.addObserver(activityLifecycleObserver)
                }
                is FeatureTarget.ApplicationTarget -> {
                    if (applicationLifecycleObserver !== DefaultObserver) {
                        ProcessLifecycleOwner.get().lifecycle.addObserver(
                            applicationLifecycleObserver
                        )
                    }
                }
                is FeatureTarget.FragmentTarget -> {
                    if (fragmentLifecycleObserver !== DefaultObserver) {
                        data.lifecycle.addObserver(fragmentLifecycleObserver)
                    }
                    if (fragmentViewLifecycleObserver !== DefaultObserver) {
                        val owner = data.viewLifecycleOwnerLiveData.value
                        if (owner != null) {
                            owner.lifecycle.addObserver(fragmentViewLifecycleObserver)
                        } else {
                            data.viewLifecycleOwnerLiveData.observe(data) {
                                it?.lifecycle?.addObserver(fragmentViewLifecycleObserver)
                            }
                        }
                    }
                }
            }
        }
    }
}