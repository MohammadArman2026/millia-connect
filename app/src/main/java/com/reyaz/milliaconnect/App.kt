package com.reyaz.milliaconnect

import android.app.Application
import com.reyaz.milliaconnect.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    /**
     * This is the application class. It's used to initialize Koin,
     * the dependency injection framework used in this application.
     */
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}