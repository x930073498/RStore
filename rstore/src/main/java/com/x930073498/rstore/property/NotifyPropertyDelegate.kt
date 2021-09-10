package com.x930073498.rstore.property

import com.x930073498.rstore.core.*
import com.x930073498.rstore.core.Feature.Companion.hasFeature
import com.x930073498.rstore.internal.*
import com.x930073498.rstore.internal.dataSaveStateKey
import com.x930073498.rstore.internal.getFeature
import com.x930073498.rstore.internal.setFeatureIImpl
import com.x930073498.rstore.property.equals.WrapEquals
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class NotifyPropertyDelegate<T : IStoreProvider, Data, Source>(
    private val provider: T,
    factory: SourceFactory<Data, Source>,
    transfer: SourceTransfer<Source, Data>,
    initializer: SourceInitializer<T, Data, Source>,
    notifier: ChangeNotifier<T, Data, Source>,
    featureProvider: FeatureProvider,
    equals: Equals<Data>
) :
    ReadWriteProperty<T, Source> {
    private val environment =
        DelegateProcess(
            factory,
            transfer,
            initializer,
            ChangeNotifier<T, Data, Source> { property, _, data, _ ->
                saveState(property, data)
            } + notifier,
            featureProvider,
            this,
            WrapEquals(transfer, equals)
        )


    private var value: Source? = null


    private fun createSource(property: KProperty<*>): Source {
        return with(environment) {
            with(factory) {
                createSource(getSaveState(property))
            }
        }
    }

    private fun initValue(property: KProperty<*>): Source {
        val result = createSource(property)
        value = result
        val data = transform(result)
        onInitialized(property, data, result)
        notify(property, data, result)
        return result
    }


    private fun equalsPre(value: Source?, property: KProperty<*>): Boolean {
        checkOrSetEquals(property)
        return with(environment) {
            equals.equals(this@NotifyPropertyDelegate.value, value)
        }
    }

    private fun transform(source: Source): Data? {
        return with(environment) {
            with(transfer) {
                transform(source)
            }
        }
    }

    private fun onInitialized(property: KProperty<*>, data: Data?, source: Source) {
        with(provider) {
            with(environment) {
                with(initializer) {
                    onInitialized(property, environment, data, source)
                }
            }
        }
    }

    private fun shouldSaveState(): Boolean {
        return with(environment) {
            with(featureProvider) {
                feature.hasFeature(Feature.SaveState)
            }
        }
    }

    private fun saveState(property: KProperty<*>, data: Data?) {
        if (provider !is ISaveStateStoreProvider) return
        if (!shouldSaveState()) return
        provider.fromSaveStateStore {
            saveState(dataSaveStateKey(property), data)
        }
    }

    private fun getSaveState(property: KProperty<*>): Data? {
        if (provider !is ISaveStateStoreProvider) return null
        if (!shouldSaveState()) return null
        return provider.fromSaveStateStore {
            getSavedState(dataSaveStateKey(property))
        }
    }

    private fun notify(property: KProperty<*>, data: Data?, source: Source) {
        with(provider) {
            with(environment) {
                with(notifier) {
                    notify(property, environment, data, source)
                }
            }
        }
    }


    private fun checkOrSetEquals(property: KProperty<*>) {
        with(provider) {
            if (!hasEquals(property)) {
                setEqualsImpl(property, environment.equals)
            }
        }
    }

    private fun compareAndSetFeature(property: KProperty<*>) {
        with(provider) {
            val feature = environment.featureProvider.feature
            val lastFeature = getFeature(property)
            if (lastFeature != feature) {
                setFeatureIImpl(property, feature)
            }
        }
    }

    override fun getValue(thisRef: T, property: KProperty<*>): Source {
        return value ?: run {
            compareAndSetFeature(property)
            checkOrSetEquals(property)
            initValue(property)
        }
    }

    override fun setValue(thisRef: T, property: KProperty<*>, value: Source) {
        when {
            value !== this.value -> {
                compareAndSetFeature(property)
                checkOrSetEquals(property)
                this.value = value
                val data = transform(value)
                onInitialized(property, data, value)
                notify(property, data, value)
            }
            !equalsPre(value, property) -> {
                compareAndSetFeature(property)
                this.value = value
                val data = transform(value)
                notify(property, data, value)
            }
            else -> {
                this.value = value
            }
        }
    }


}