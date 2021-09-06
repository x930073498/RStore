package com.x930073498.sample

import com.x930073498.features.core.Feature

interface TestFeature : Feature {
    companion object:TestFeature{
        override fun print() {
            println("enter this line 9999")
        }

    }
    fun print()
}
