/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.x930073498.features.internal;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;


@SuppressWarnings({"UnknownNullness", "deprecation"})
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
public class ReportFragment extends android.app.Fragment {
    private static final String REPORT_FRAGMENT_TAG = "d7c73fb1-db79-42b3-8e10-45894b584b69";


    public static void getReportFragment(@NonNull Activity activity) {
        ActivityLifecycleOwner owner = ActivityLifecycleOwner.get(activity);
        if (owner != null) {
            owner.getFragment();
            return;
        }
        android.app.FragmentManager manager = activity.getFragmentManager();
        ReportFragment fragment = (ReportFragment) manager.findFragmentByTag(REPORT_FRAGMENT_TAG);
        if (fragment == null) {
            fragment = new ReportFragment();
            ActivityLifecycleOwner.put(new ActivityLifecycleOwner(activity, fragment));
            manager.beginTransaction().add(fragment, REPORT_FRAGMENT_TAG).commit();
            manager.executePendingTransactions();
        } else {
            ActivityLifecycleOwner.put(new ActivityLifecycleOwner(activity, fragment));
        }
    }

    public static void injectIfNeededIn(Activity activity) {
        if (Build.VERSION.SDK_INT >= 29) {
            LifecycleCallbacks.registerIn(activity);
        }
        getReportFragment(activity);
    }




    static void dispatch(Activity activity, @NonNull Lifecycle.Event event) {
        LifecycleOwner owner = ActivityLifecycleOwner.get(activity);
        if (owner != null) {
            ((LifecycleRegistry) owner.getLifecycle()).handleLifecycleEvent(event);
        }

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dispatch(Lifecycle.Event.ON_CREATE);
    }

    @Override
    public void onStart() {
        super.onStart();
        dispatch(Lifecycle.Event.ON_START);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        dispatch(Lifecycle.Event.ON_RESUME);
    }

    @Override
    public void onPause() {
        super.onPause();
        dispatch(Lifecycle.Event.ON_PAUSE);
    }

    @Override
    public void onStop() {
        super.onStop();
        dispatch(Lifecycle.Event.ON_STOP);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dispatch(Lifecycle.Event.ON_DESTROY);
        // just want to be sure that we won't leak reference to an activity
    }

    private void dispatch(@NonNull Lifecycle.Event event) {
        if (Build.VERSION.SDK_INT < 29) {
            // Only dispatch events from ReportFragment on API levels prior
            // to API 29. On API 29+, this is handled by the ActivityLifecycleCallbacks
            // added in ReportFragment.injectIfNeededIn
            dispatch(getActivity(), event);
        }
    }


    // this class isn't inlined only because we need to add a proguard rule for it (b/142778206)
    // In addition to that registerIn method allows to avoid class verification failure,
    // because registerActivityLifecycleCallbacks is available only since api 29.
    @RequiresApi(29)
    static class LifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

        static void registerIn(Activity activity) {
            activity.registerActivityLifecycleCallbacks(new LifecycleCallbacks());
        }

        @Override
        public void onActivityCreated(@NonNull Activity activity,
                                      @Nullable Bundle bundle) {
        }

        @Override
        public void onActivityPostCreated(@NonNull Activity activity,
                                          @Nullable Bundle savedInstanceState) {
            dispatch(activity, Lifecycle.Event.ON_CREATE);
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
        }

        @Override
        public void onActivityPostStarted(@NonNull Activity activity) {
            dispatch(activity, Lifecycle.Event.ON_START);
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
        }

        @Override
        public void onActivityPostResumed(@NonNull Activity activity) {
            dispatch(activity, Lifecycle.Event.ON_RESUME);
        }

        @Override
        public void onActivityPrePaused(@NonNull Activity activity) {
            dispatch(activity, Lifecycle.Event.ON_PAUSE);
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
        }

        @Override
        public void onActivityPreStopped(@NonNull Activity activity) {
            dispatch(activity, Lifecycle.Event.ON_STOP);
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity,
                                                @NonNull Bundle bundle) {
        }

        @Override
        public void onActivityPreDestroyed(@NonNull Activity activity) {
            dispatch(activity, Lifecycle.Event.ON_DESTROY);
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
        }
    }
}
