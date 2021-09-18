package com.x930073498.sample

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.x930073498.features.core.ActivityFeatureLifecycleObserver
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.ApplicationFeatureLifecycleObserver
import com.x930073498.features.core.FragmentFeatureLifecycleObserver
import com.x930073498.features.installInstanceFeature
import com.x930073498.features.remove

class App : Application(), TestFeature {
    override fun onFeatureInitialized(target: FeatureTarget) {
        println("enter this line Application")
    }
    override fun onCreate() {
        super.onCreate()



        installInstanceFeature<TestFeature> {
            with(target) {
                when (this) {
                    is FeatureTarget.ActivityTarget -> {
                        addObserver(object : ActivityFeatureLifecycleObserver {
                            override fun onActivityCreated(
                                activity: Activity,
                                savedInstanceState: Bundle?
                            ) {
                                feature.print()
                            }
                        })
                    }
                    is FeatureTarget.ApplicationTarget -> {
                        addObserver(object :DefaultLifecycleObserver{
                            override fun onCreate(owner: LifecycleOwner) {

                            }
                        })
                        addObserver(object : ApplicationFeatureLifecycleObserver {
                            override fun onApplicationCreated(application: Application) {
                                feature.print()
                            }
                        })
                    }
                    is FeatureTarget.FragmentTarget -> {
                        addObserver(object : FragmentFeatureLifecycleObserver {
                            override fun onFragmentPreAttached(
                                fm: FragmentManager,
                                f: Fragment,
                                context: Context
                            ) {
                                feature.print()
                            }
                        })
                    }
                }
            }
        }
    }

    override fun print() {
        println("enter this line application=$this")
    }

}