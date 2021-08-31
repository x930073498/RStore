package com.x930073498.rstore

import android.app.Application
import android.content.Context
import androidx.startup.Initializer

internal lateinit var app: Application

class StarterInitializer : Initializer<Application> {
    override fun create(context: Context): Application {
        context as Application
        if (!::app.isInitialized) {
            app = context
        }
        return app
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}