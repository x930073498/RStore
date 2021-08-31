@file:Suppress("FunctionName")

package com.x930073498.rstore.core

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.x930073498.rstore.LifecycleAnchorStarter
import com.x930073498.rstore.SaveStateHandleStore
import com.x930073498.rstore.SaveStateStoreViewModel
import com.x930073498.rstore.app
import com.x930073498.rstore.internal.awaitUntilImpl
import com.x930073498.rstore.internal.registerAnchorPropertyChangedListenerImpl
import com.x930073498.rstore.internal.registerPropertyChangedListenerImpl
import com.x930073498.rstore.internal.setSavedStateRegistryOwner
import com.x930073498.rstore.property.lazyField
import kotlin.properties.ReadOnlyProperty

import kotlin.reflect.KProperty

interface StoreComponent : SavedStateRegistryOwner {

    suspend fun <T : IStoreProvider, V> T.awaitUntil(
        property: KProperty<V>,
        predicate: suspend V.() -> Boolean
    ) = with(this@StoreComponent) {
        setSavedStateRegistryOwner(this)
        awaitUntilImpl(property, predicate)
    }

    fun <T : IStoreProvider, V> T.withProperty(
        property: KProperty<V>,
        lifecycleOwner: LifecycleOwner = defaultLifecycleOwner,
        action: V.() -> Unit
    ) = with(this@StoreComponent) {
        setSavedStateRegistryOwner(this)
        registerPropertyChangedListenerImpl(property, lifecycleOwner, action)
    }


    fun <T : IStoreProvider> T.withAnchor(
        starter: AnchorStarter = LifecycleAnchorStarter(
            defaultLifecycleOwner,
            false
        ),
        action: AnchorScope<T>.(T) -> Unit
    ) = with(this@StoreComponent) {
        setSavedStateRegistryOwner(this)
        registerAnchorPropertyChangedListenerImpl(starter, action)
    }

}

val StoreComponent.defaultLifecycleOwner: LifecycleOwner
    get() {
        return when (this) {
            is Fragment -> {
                viewLifecycleOwnerLiveData.value ?: this
            }
            else -> this
        }
    }
val StoreComponent.application: Application
    get() {
        return when (this) {
            is Activity -> application
            is Fragment -> activity?.application ?: app
            else -> app
        }
    }
val StoreComponent.savedStateViewModelFactory: ViewModelProvider.Factory by lazyField {
    if (this is HasDefaultViewModelProviderFactory) {
        val defaultFactory = defaultViewModelProviderFactory
        if (defaultFactory is SavedStateViewModelFactory) {
            defaultFactory
        } else {
            SavedStateViewModelFactory(application, this)
        }
    } else {
        SavedStateViewModelFactory(application, this)
    }
}

inline fun <reified T> savedStateViewModels() where T : ISaveStateStoreProvider, T : ViewModel =
    savedStateViewModels(T::class.java)

@PublishedApi
internal fun <T> savedStateViewModels(target: Class<T>): ReadOnlyProperty<StoreComponent, T> where T : ViewModel, T : ISaveStateStoreProvider {
    return lazyField {
        when (this) {
            is ViewModelStoreOwner -> {
                ViewModelProvider(this, savedStateViewModelFactory).get(target)
            }
            is ViewModelStore -> {
                ViewModelProvider(this, savedStateViewModelFactory).get(target)
            }
            else -> {
                throw RuntimeException("请保证StoreComponent对象为ViewModelStoreOwner 或ViewModelStore")
            }
        }
    }
}

val StoreComponent.storeProvider by savedStateViewModels<SaveStateStoreViewModel>()

val StoreComponent.context: Context by lazyField {
    when (this) {
        is Activity -> this
        is Fragment -> requireContext()
        else -> application
    }
}

fun StoreComponent.LifecycleAnchorStarter(onCreateResume: () -> Boolean): AnchorStarter {
    return LifecycleAnchorStarter(defaultLifecycleOwner, onCreateResume)
}

fun StoreComponent.LifecycleAnchorStarter(onCreateResume: Boolean = false): AnchorStarter {
    return LifecycleAnchorStarter(defaultLifecycleOwner, onCreateResume)
}
