package com.x930073498.sample

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.x930073498.features.core.Feature
import com.x930073498.features.core.FeatureInstaller
import com.x930073498.features.core.FeatureLifecycle
import com.x930073498.features.core.FeatureLifecycleObserver

interface TestFeature :Feature{
}
class TestFeatureInstaller:FeatureInstaller<TestFeature>{
    override fun onInstall(feature: TestFeature, lifecycle: FeatureLifecycle) {
        lifecycle.addObserver(object :FeatureLifecycleObserver{
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                println("enter this line TestFeatureInstaller activity=$activity")
            }

            override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
                println("enter this line TestFeatureInstaller fragment=$f")
            }
        })

    }

}