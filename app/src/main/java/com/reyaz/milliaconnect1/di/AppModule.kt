package com.reyaz.milliaconnect1.di

import androidx.work.WorkManager
import com.reyaz.milliaconnect1.util.NotificationHelper
import com.reyaz.milliaconnect1.data.UserPreferences
import com.reyaz.milliaconnect1.data.WebLoginManager
import com.reyaz.milliaconnect1.ui.screen.VMLogin
import com.reyaz.milliaconnect1.util.NetworkConnectivityObserver
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModule = module {
    single { UserPreferences(get()) }
//    single { WifiNetworkManager(get()) }
    single { WebLoginManager(get(), get()) }
    single { NetworkConnectivityObserver(get()) }
    viewModel { VMLogin(get(), get(), get(), get()) }
    single { WorkManager.getInstance(get()) }
    single { NotificationHelper(get()) }
}