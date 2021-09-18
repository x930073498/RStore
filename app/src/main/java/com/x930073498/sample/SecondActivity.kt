package com.x930073498.sample

import android.app.Activity
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.Removable
import com.x930073498.features.installInstanceFeature

class SecondActivity : Activity(), SecondTestFeature {
    private lateinit var removable: Removable


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        removable = installInstanceFeature<SecondTestFeature> {
            with(target) {
                if (this is FeatureTarget.ActivityTarget) {
                    addObserver(object : DefaultLifecycleObserver {
                        override fun onCreate(owner: LifecycleOwner) {
                            feature.printCreate()
                        }

                        override fun onStart(owner: LifecycleOwner) {
                            feature.printStart()
                        }

                        override fun onResume(owner: LifecycleOwner) {
                            feature.printResume()
                        }

                        override fun onPause(owner: LifecycleOwner) {
                            feature.printPause()
                        }

                        override fun onStop(owner: LifecycleOwner) {
                            feature.printStop()
                        }

                        override fun onDestroy(owner: LifecycleOwner) {
                            feature.printDestroy()
                        }
                    })
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removable.remove()
    }

    override fun onFeatureInitialized(target: FeatureTarget) {
        println("enter this line 14747")
    }

    override fun printCreate() {
        println("enter this line create")
    }

    override fun printStart() {
        println("enter this line start")
    }

    override fun printResume() {
        println("enter this line resume")
    }

    override fun printPause() {
        println("enter this line pause")
    }

    override fun printStop() {
        println("enter this line stop")
    }

    override fun printDestroy() {
        println("enter this line destroy")
    }

}