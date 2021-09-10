@file:Suppress("FunctionName")

package com.x930073498.rstore.core

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.x930073498.rstore.LifecycleAnchorStarter
import com.x930073498.rstore.SaveStateStoreViewModel
import com.x930073498.rstore.app
import com.x930073498.rstore.LifecyclePropertyChangeStater
import com.x930073498.rstore.internal.awaitUntilImpl
import com.x930073498.rstore.internal.registerAnchorPropertyChangedListenerImpl
import com.x930073498.rstore.internal.registerPropertyChangedListenerImpl
import com.x930073498.rstore.property.lazyField
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

interface StoreComponent : SavedStateRegistryOwner, Coroutine {
    override val coroutineScope: CoroutineScope
        get() = lifecycleScope
    override val io: CoroutineContext
        get() = storeProvider.io
    override val main: CoroutineContext
        get() = storeProvider.main

    suspend fun <T : IStoreProvider, V> T.awaitUntil(
        property: KProperty<V>,
        predicate: suspend V.() -> Boolean
    ) = awaitUntilImpl(property, predicate)


    fun <T : IStoreProvider, V> T.withProperty(
        property: KProperty<V>,
        stater: PropertyChangeStater = LifecyclePropertyChangeStater(),
        action: V.() -> Unit
    ) = registerPropertyChangedListenerImpl(property, stater, action)


    fun <T : IStoreProvider> T.withAnchor(
        starter: AnchorStarter = LifecycleAnchorStarter(
            defaultLifecycleOwner,
            false
        ),
        action: AnchorScope<T>.(T) -> Unit
    ) = registerAnchorPropertyChangedListenerImpl(starter, action)


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
    it.bindLifecycle(defaultLifecycleOwner)
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
        it.bindLifecycle(defaultLifecycleOwner)
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
    it.bindLifecycle(defaultLifecycleOwner)
    when (this) {
        is Activity -> this
        is Fragment -> requireContext()
        else -> application
    }
}

fun StoreComponent.LifecycleAnchorStarter(
    lifecycleOwner: LifecycleOwner = defaultLifecycleOwner,
    onCreateResume: () -> Boolean
): AnchorStarter {
    return com.x930073498.rstore.LifecycleAnchorStarter(lifecycleOwner,onCreateResume)
}

fun StoreComponent.LifecycleAnchorStarter(
    onCreateResume: Boolean = false,
    lifecycleOwner: LifecycleOwner = defaultLifecycleOwner,
): AnchorStarter {
    return LifecycleAnchorStarter(lifecycleOwner, onCreateResume)
}

fun StoreComponent.LifecyclePropertyChangeStater(
    lifecycleOwner: LifecycleOwner = defaultLifecycleOwner,
    onCreateResume: () -> Boolean
): PropertyChangeStater {
    return LifecyclePropertyChangeStater(lifecycleOwner, onCreateResume)
}

fun StoreComponent.LifecyclePropertyChangeStater(
    onCreateResume: Boolean = false,
    lifecycleOwner: LifecycleOwner = defaultLifecycleOwner,
): PropertyChangeStater {
    return LifecyclePropertyChangeStater(lifecycleOwner, onCreateResume)
}
