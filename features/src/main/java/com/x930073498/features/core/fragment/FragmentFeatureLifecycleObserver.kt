package com.x930073498.features.core.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

interface FragmentFeatureLifecycleObserver {
    companion object:FragmentFeatureLifecycleObserver

    fun onFragmentPreAttached(fm: FragmentManager, f: Fragment, context: Context) {
    }

    fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
    }

    fun onFragmentPreCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
    }

    fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
    }

    fun onFragmentActivityCreated(
        fm: FragmentManager,
        f: Fragment,
        savedInstanceState: Bundle?
    ) {
    }

    fun onFragmentViewCreated(
        fm: FragmentManager,
        f: Fragment,
        v: View,
        savedInstanceState: Bundle?
    ) {
    }

    fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
    }

    fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
    }

    fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
    }
}