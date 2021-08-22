package com.x930073498.rstore.property

import com.x930073498.rstore.Equals
import com.x930073498.rstore.core.IStoreProvider

 data class DelegateProcess<T : IStoreProvider, Data, Source>(
    val factory: SourceFactory<T, Data, Source>,
    val initializer: SourceInitializer<T, Data, Source>,
    val notifier: ChangeNotifier<T, Data, Source>,
    val checker: StateChecker<T, Data, Source>,
    val equals: Equals<Source>

)