package com.x930073498.features

import android.app.Application
import android.content.Context
import androidx.startup.Initializer
import com.x930073498.features.internal.Features


internal lateinit var app: Application

class FeatureStartup : Initializer<Application> {
    override fun create(context: Context): Application {
        context as Application
        if (!::app.isInitialized) {
            app = context
            Features.setup(app)
        }
        return app
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return mutableListOf()
    }
}