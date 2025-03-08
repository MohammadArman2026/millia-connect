package com.reyaz.milliaconnect1

import android.app.Application
import com.reyaz.milliaconnect1.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin for dependency injection
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}