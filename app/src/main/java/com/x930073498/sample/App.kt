package com.x930073498.sample

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.x930073498.features.addFeatureInitializer
import com.x930073498.features.core.*
import com.x930073498.lifecycle.core.Removable

class App : Application(), TestFeature {

    private var removable: Removable = Removable
    override fun onCreate() {
        removable = addFeatureInitializer(false) {
            addTypeInstanceHandler<TestFeature> {
                addLifecycleObserver(object : DefaultLifecycleObserver {
                    override fun onResume(owner: LifecycleOwner) {
                        feature.print()
                    }
                })
            }
            addActivityWrapperFeatureHandler(SecondActivity::class.java,this@App){
                feature.print()
            }
        }
        super.onCreate()


    }

    override fun onLowMemory() {
        super.onLowMemory()

    }


    override fun print() {
        println("enter this line application=$this")
    }

}