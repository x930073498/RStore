package com.x930073498.rstore.property

import com.x930073498.rstore.core.Equals
import com.x930073498.rstore.core.ISaveStateStoreProvider
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.core.fromSaveStateStore
import com.x930073498.rstore.core.getSavedState
import com.x930073498.rstore.core.saveState
import com.x930073498.rstore.internal.dataSaveStateKey
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class NotifyPropertyDelegate<T : IStoreProvider, Data, Source>(
    private val provider: T,
    factory: SourceFactory<T, Data, Source>,
    initializer: SourceInitializer<T, Data, Source>,
    notifier: ChangeNotifier<T, Data, Source>,
    checker: StateChecker<T, Data, Source>,
    equals: Equals<Data>
) :
    ReadWriteProperty<T, Source>{
    private val environment =
        DelegateProcess(
            factory,
            initializer,
            notifier + ChangeNotifier { property, _, data, _ -> saveState(property, data) },
            checker,
            this,
            equals
        )


    private var value: Source? = null


    private fun createSource(property: KProperty<*>): Source {
        return with(provider) {
            with(environment) {
                with(factory) {
                    createSource(property, environment, getSaveState(property))
                }
            }
        }
    }

    private fun initValue(property: KProperty<*>): Source {
        val result = createSource(property)
        value = result
        val data = transform(property, result)
        onInitialized(property, data, result)
        notify(property, data, result)
        return result
    }


    private fun equalsPre(value: Source?, property: KProperty<*>): Boolean {
        return with(provider) {
            with(environment) {
                equals(property, this@NotifyPropertyDelegate.value, value)
            }
        }
    }

    private fun transform(property: KProperty<*>, source: Source): Data? {
        return with(provider) {
            with(environment) {
                with(factory) {
                    transform(property, environment, source)
                }
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

    private fun shouldSaveState(property: KProperty<*>): Boolean {
        return with(provider) {
            with(environment) {
                with(checker) {
                    shouldSaveState(property, environment)
                }
            }
        }
    }

    private fun saveState(property: KProperty<*>, data: Data?) {
        if (provider !is ISaveStateStoreProvider) return
        if (!shouldSaveState(property)) return
        provider.fromSaveStateStore {
            saveState(dataSaveStateKey(id, property), data)
        }
    }

    private fun getSaveState(property: KProperty<*>): Data? {
        if (provider !is ISaveStateStoreProvider) return null
        if (!shouldSaveState(property)) return null
        return provider.fromSaveStateStore {
            getSavedState(dataSaveStateKey(id, property))
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


    override fun getValue(thisRef: T, property: KProperty<*>): Source {
        return value ?: initValue(property)
    }

    override fun setValue(thisRef: T, property: KProperty<*>, value: Source) {
        when {
            value !== this.value -> {
                this.value = value
                val data = transform(property, value)
                onInitialized(property, data, value)
                notify(property, data, value)
            }
            !equalsPre(value, property) -> {
                this.value = value
                val data = transform(property, value)
                notify(property, data, value)
            }
            else -> {
                this.value = value
            }
        }
    }


}