package com.x930073498.features

import android.app.Activity
import androidx.fragment.app.Fragment
import com.x930073498.features.core.Feature
import com.x930073498.features.core.FeatureInstaller
import com.x930073498.features.core.activity.ActivityFeatureInstaller
import com.x930073498.features.core.application.ApplicationFeatureInstaller
import com.x930073498.features.core.fragment.FragmentFeatureInstaller
import com.x930073498.features.internal.FeatureRepository


inline fun <reified T : Feature> installFeature(installer: FeatureInstaller<T>) {
    FeatureRepository.addFeatureInstaller(T::class.java, installer)
}
inline fun <reified T : Feature> installActivityFeature(installer: ActivityFeatureInstaller<T>) {
    FeatureRepository.addFeatureInstaller(T::class.java, installer)
}
inline fun <reified T : Feature> installFragmentFeature(installer: FragmentFeatureInstaller<T>) {
    FeatureRepository.addFeatureInstaller(T::class.java, installer)
}
fun <T:Feature> installApplicationFeature(feature: T,installer:ApplicationFeatureInstaller<T>){
    FeatureRepository.addFeatureInstaller(feature,installer)
}


inline fun <T : Feature, reified V : Activity> installFeature(
    feature: T,
    installer: ActivityFeatureInstaller<T>
) {
    FeatureRepository.addFeatureInstaller(V::class.java, feature, installer)
}

inline fun <T : Feature, reified V : Fragment> installFeature(
    feature: T,
    installer: FragmentFeatureInstaller<T>
) {
    FeatureRepository.addFeatureInstaller(V::class.java, feature, installer)
}