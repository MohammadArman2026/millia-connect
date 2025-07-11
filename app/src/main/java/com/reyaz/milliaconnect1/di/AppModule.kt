package com.reyaz.milliaconnect1.di

import androidx.work.WorkManager
import com.reyaz.milliaconnect1.util.NetworkConnectivityObserver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val appModule = module {
    single { NetworkConnectivityObserver(get()) }
    single { WorkManager.getInstance(androidContext()) }
}