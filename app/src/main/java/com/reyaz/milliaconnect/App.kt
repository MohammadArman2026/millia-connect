package com.reyaz.milliaconnect

import android.app.Application
import android.content.IntentFilter
import android.net.ConnectivityManager
import com.reyaz.milliaconnect.di.appModule
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