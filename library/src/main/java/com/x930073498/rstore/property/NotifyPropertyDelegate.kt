package com.x930073498.rstore.property

import com.x930073498.rstore.Equals
import com.x930073498.rstore.core.ISaveStateStoreProvider
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.core.getSavedState
import com.x930073498.rstore.core.saveState
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
    ReadWriteProperty<T, Source> {
    private val environment = DelegateProcess(factory, initializer, notifier, checker, equals)

    private var value: Source? = null


    private fun initValue(property: KProperty<*>): Source {
        return with(provider) {
            with(environment) {
                with(factory) {
                    var pre: Data? = null
                    if (property is ISaveStateStoreProvider && with(checker) {
                            shouldSaveState(property, environment)
                        }) {
                        pre = property.getSavedState<Data>(dataSaveStateKey(property))
                    }
                    createSource(property, environment, pre).apply {
                        value = this
                        val data: Data? = transform(property, environment, this)
                        with(initializer) {
                            onInitialized(property, environment, data, this@apply)
                        }
                        with(notifier) {
                            notify(property, environment, data, this@apply)
                        }
                    }
                }
            }

        }
    }

    override fun getValue(thisRef: T, property: KProperty<*>): Source {
        return value ?: initValue(property)
    }

    private fun equalsPre(value: Source?, property: KProperty<*>): Boolean {
        return with(provider) {
            with(environment) {
                equals(property, this@NotifyPropertyDelegate.value, value)
            }
        }
    }

    override fun setValue(thisRef: T, property: KProperty<*>, value: Source) {
        when {
            value !== this.value -> {
                this.value = value
                with(thisRef) {
                    with(environment) {
                        with(factory) {
                            with(initializer) {
                                onInitialized(
                                    property, environment,
                                    transform(property, environment, value), value
                                )
                                with(notifier) {
                                    notify(
                                        property,
                                        environment,
                                        transform(property, environment, value),
                                        value
                                    )
                                }
                            }

                        }
                    }
                }
            }
            !equalsPre(value, property) -> {
                this.value = value
                with(thisRef) {
                    with(environment) {
                        with(factory) {
                            with(notifier) {
                                notify(
                                    property,
                                    environment,
                                    transform(property, environment, value),
                                    value
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                this.value = value
            }
        }
    }
}