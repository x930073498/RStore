package com.x930073498.features.core

 interface IFeature {
    fun onFeatureInitialized(target: FeatureTarget){}

    companion object : IFeature
}