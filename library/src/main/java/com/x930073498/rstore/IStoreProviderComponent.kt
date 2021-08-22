package com.x930073498.rstore

import com.x930073498.rstore.core.ISaveStateStoreProvider
import com.x930073498.rstore.core.IStoreProvider

interface IStoreProviderComponent : IStoreProvider, ISaveStateStoreProvider {
}