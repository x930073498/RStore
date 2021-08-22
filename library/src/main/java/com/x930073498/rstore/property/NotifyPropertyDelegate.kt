package com.x930073498.rstore.property

import com.x930073498.rstore.Equals
import com.x930073498.rstore.core.ISaveStateStoreProvider
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.core.getSavedState
import com.x930073498.rstore.core.saveState
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class NotifyPropertyDelegate<T : IStoreProvider, Data, Source>(
    private val factory: SourceFactory<T, Data, Source>,
    private val initializer: SourceInitializer<T, Data, Source>,
    private val notifier: ChangeNotifier<T, Data, Source>,
    private val checker: StateChecker<T, Data, Source>,
    private val equals: Equals<Source>

) :
    ReadWriteProperty<T, Source> {
    private var value: Source? = null

    private val environment = DelegateProcess(factory, initializer, notifier, checker, equals)


    override fun getValue(thisRef: T, property: KProperty<*>): Source {
        return value ?: with(thisRef) {
            with(factory) {
                var pre: Data? = null
                if (thisRef is ISaveStateStoreProvider && with(checker) {
                        shouldSaveState(property, environment)
                    }) {
                    pre = thisRef.getSavedState<Data>(dataSaveStateKey(property))
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

    override fun setValue(thisRef: T, property: KProperty<*>, value: Source) {
        when {
            value !== this.value -> {
                this.value = value
                with(thisRef) {
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
            !equals.equals(value, this.value) -> {
                this.value = value
                with(thisRef) {
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
            else -> {
                this.value = value
            }
        }
    }
}