package com.reyaz.milliaconnect1

import android.app.Application
import com.reyaz.feature.attendance.schedule.di.scheduleModule
import com.reyaz.feature.portal.di.portalModule
import com.reyaz.feature.result.di.resultModule
import com.reyaz.milliaconnect1.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * The Application class is the first component of your app to be instantiated when the process starts.
 * It has the longest lifecycle, making it the ideal place to set up global,
 * app-wide dependencies that need to be available from the very beginning.
 * Base class for the application.
 *
 * This class extends the Android [Application] class and is responsible for
 * initializing application-wide components, such as Koin for dependency injection.
 */
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Koin for dependency injection
        startKoin {
            androidContext(this@BaseApplication)
            modules(appModule, scheduleModule, portalModule, resultModule)
        }
    }
}