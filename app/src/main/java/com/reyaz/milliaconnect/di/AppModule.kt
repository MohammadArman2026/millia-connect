package com.reyaz.milliaconnect.di

import androidx.work.WorkManager
import com.reyaz.milliaconnect.util.NotificationHelper
import com.reyaz.milliaconnect.data.UserPreferences
import com.reyaz.milliaconnect.data.WebLoginManager
import com.reyaz.milliaconnect.ui.screen.VMLogin
import com.reyaz.milliaconnect.util.NetworkConnectivityObserver
import com.reyaz.milliaconnect.util.WifiNetworkManager
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single { UserPreferences(get()) }
    single { WifiNetworkManager(get()) }
    single { WebLoginManager(get(), get()) }
    single { NetworkConnectivityObserver(get()) }
    viewModel { VMLogin(get(), get(), get(), get()) }
    single { WorkManager.getInstance(get()) }
    single { NotificationHelper(get()) }
}