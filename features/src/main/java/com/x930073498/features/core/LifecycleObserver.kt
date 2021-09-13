package com.x930073498.features.core

interface LifecycleObserver {
    enum class State {
        ON_CREATE, ON_RESUME, ON_START, ON_PAUSE, ON_STOP
    }

    fun onStateChange(state: State){

    }
}