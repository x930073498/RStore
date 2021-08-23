package com.x930073498.rstore

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.x930073498.rstore.core.IStoreProvider
import kotlin.reflect.KProperty

interface StoreComponent : LifecycleOwner

internal val StoreComponent.defaultLifecycleOwner: LifecycleOwner
    get() {
        return when (this) {
            is Activity -> this
            is Fragment -> {
                viewLifecycleOwnerLiveData.value ?: this
            }
            else -> this
        }
    }