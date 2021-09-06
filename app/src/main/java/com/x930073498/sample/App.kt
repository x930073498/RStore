package com.x930073498.sample

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import com.x930073498.features.core.activity.ActivityFeatureStateObserver
import com.x930073498.features.core.application.ApplicationFeatureLifecycleObserver
import com.x930073498.features.installActivityFeature
import com.x930073498.features.installApplicationFeature

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        installActivityFeature(MainActivity::class.java, TestFeature) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                target.featureLifecycle.addObserver(object : ActivityFeatureStateObserver {
                    override fun onActivityPreCreated(
                        activity: Activity,
                        savedInstanceState: Bundle?
                    ) {
                        feature.print()
                    }
                })
            }

        }
//        installApplicationFeature(TestFeature) {
//            this.target.featureLifecycle.addObserver(object :ApplicationFeatureLifecycleObserver{
//                override fun onApplicationPaused() {
//                    feature.print()
//                }
//            })
//        }
    }

}