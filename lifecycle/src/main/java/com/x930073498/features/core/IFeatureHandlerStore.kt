package com.x930073498.features.core

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.x930073498.features.internal.addHandler

interface IFeatureHandlerStore


fun <F : IFeature> IFeatureHandlerStore.addActivityHandler(
    parser: FeatureParser<Activity, F>,
    action: FeatureAction<Activity, F>
) = addHandler(Activity::class.java, parser, action)


fun <F : IFeature> IFeatureHandlerStore.addFragmentHandler(
    parser: FeatureParser<Fragment, F>,
    action: FeatureAction<Fragment, F>
) = addHandler(Fragment::class.java, parser, action)


fun <F : IFeature> IFeatureHandlerStore.addApplicationHandler(
    parser: FeatureParser<Application, F>,
    action: FeatureAction<Application, F>
) = addHandler(
    Application::class.java,
    parser,
    action
)

inline fun <reified F : IFeature> IFeatureHandlerStore.addTypeInstanceHandler(action: FeatureAction<Any, F>) =
    addHandler(
        Any::class.java,
        TypeInstanceFeatureParser(F::class.java),
        action
    )

fun <C : Activity, F : IFeature> IFeatureHandlerStore.addActivityWrapperFeatureHandler(
    targetClass: Class<C>,
    feature: F,
    action: FeatureAction<C, F>
) = addHandler(targetClass, WrapperFeatureParser(targetClass, feature), action)

fun <C : Fragment, F : IFeature> IFeatureHandlerStore.addFragmentWrapperFeatureHandler(
    targetClass: Class<C>,
    feature: F,
    action: FeatureAction<C, F>
) = addHandler(targetClass, WrapperFeatureParser(targetClass, feature), action)

fun <F : IFeature> IFeatureHandlerStore.addApplicationWrapperFeatureHandler(
    feature: F,
    action: FeatureAction<Application, F>
) = addHandler(
    Application::class.java,
    WrapperFeatureParser(Application::class.java, feature),
    action
)
