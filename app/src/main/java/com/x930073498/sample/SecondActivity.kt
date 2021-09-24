package com.x930073498.sample

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.x930073498.lifecycle.addInitializer
import com.x930073498.lifecycle.castActivity
import com.x930073498.lifecycle.core.*

class SecondActivity : Activity(), SecondTestFeature {
//    private lateinit var removable: Removable
private var removable:Removable=Removable

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        findViewById<View>(Window.ID_ANDROID_CONTENT).setOnClickListener {
            startActivity(Intent(this,ThirdActivity::class.java))
        }
//        removable = installInstanceFeature<SecondTestFeature> {
//            with(target) {
//                if (this is FeatureTarget.ActivityTarget) {
//                    addObserver(object : DefaultLifecycleObserver {
//                        override fun onCreate(owner: LifecycleOwner) {
//                            feature.printCreate()
//                        }
//
//                        override fun onStart(owner: LifecycleOwner) {
//                            feature.printStart()
//                        }
//
//                        override fun onResume(owner: LifecycleOwner) {
//                            feature.printResume()
//                        }
//
//                        override fun onPause(owner: LifecycleOwner) {
//                            feature.printPause()
//                        }
//
//                        override fun onStop(owner: LifecycleOwner) {
//                            feature.printStop()
//                        }
//
//                        override fun onDestroy(owner: LifecycleOwner) {
//                            feature.printDestroy()
//                        }
//                    })
//                }
//            }
//        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removable.remove()
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