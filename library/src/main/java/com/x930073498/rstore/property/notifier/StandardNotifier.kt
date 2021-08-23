package com.x930073498.rstore.property.notifier

import com.x930073498.rstore.core.ISaveStateStoreProvider
import com.x930073498.rstore.core.IStoreProvider
import com.x930073498.rstore.core.saveState
import com.x930073498.rstore.property.*
import com.x930073498.rstore.property.dataSaveStateKey
import com.x930073498.rstore.property.equals.TransformEquals
import com.x930073498.rstore.property.notifyPropertyChanged
import kotlin.reflect.KProperty

internal class StandardNotifier<T : IStoreProvider, Data, Source> :
    ChangeNotifier<T, Data, Source> {
    override fun T.notify(
        property: KProperty<*>,
        process: DelegateProcess<T, Data, Source>,
        data: Data?,
        source: Source?
    ) {
        val equals = TransformEquals(this, property, process)
        notifyPropertyChanged(property, equals)
        with(process) {
            with(checker) {
                if (isAnchorProperty(property, process)) {
                    notifyAnchorPropertyChanged(property, equals)
                }
                if (shouldSaveState(property, process) && this@notify is ISaveStateStoreProvider) {
                    saveState(dataSaveStateKey(property), data)
                }
            }
        }
    }
}