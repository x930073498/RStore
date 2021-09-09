package com.x930073498.sample

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.activity.ActivityFeatureLifecycleObserver
import com.x930073498.features.core.activity.ActivityFeatureStateObserver
import com.x930073498.features.core.application.ApplicationFeatureLifecycleObserver
import com.x930073498.features.core.fragment.FragmentFeatureLifecycleObserver
import com.x930073498.features.installActivityFeature
import com.x930073498.features.installApplicationFeature
import com.x930073498.features.installInstanceFeature

class App : Application(),TestFeature {
    override fun onCreate() {
        super.onCreate()
        installInstanceFeature<TestFeature> {
            when(it){
                is FeatureTarget.ActivityTarget -> {
                    it.featureLifecycle.addObserver(object:ActivityFeatureLifecycleObserver{
                        override fun onActivityResumed(activity: Activity) {
                            print()
                        }
                    })
                }
                is FeatureTarget.ApplicationTarget -> {
                    it.featureLifecycle.addObserver(object :ApplicationFeatureLifecycleObserver{
                        override fun onApplicationResumed(application: Application) {
                            print()
                        }
                    })
                }
                is FeatureTarget.FragmentTarget ->{
                    it.featureLifecycle.addObserver(object :FragmentFeatureLifecycleObserver{
                        override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
                            print()
                        }
                    })
                }
            }
        }
    }

    override fun print() {
        println("enter this line application=$this")
    }

}