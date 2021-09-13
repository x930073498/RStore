package com.x930073498.sample

import com.x930073498.features.core.FeatureTarget
import com.x930073498.features.core.IFeature

interface TestFeature : IFeature {
    override fun onFeatureInitialized(target: FeatureTarget) {
println("enter this line 87")
    }
    companion object:TestFeature{
        override fun print() {
            println("enter this line 9999")
        }

    }
    fun print()


}
