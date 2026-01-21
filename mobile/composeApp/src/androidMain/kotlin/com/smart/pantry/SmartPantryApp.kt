package com.smart.pantry

import android.app.Application
import com.smart.pantry.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Android Application class
 * Initializes Koin dependency injection
 */
class SmartPantryApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin
        startKoin {
            // Log Koin into Android logger
            androidLogger(Level.ERROR)
            // Reference Android context
            androidContext(this@SmartPantryApp)
            // Load modules
            modules(appModules)
        }
    }
}
