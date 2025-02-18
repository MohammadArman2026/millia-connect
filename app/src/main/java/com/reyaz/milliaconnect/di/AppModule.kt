package com.reyaz.milliaconnect.di

import androidx.work.WorkManager
import com.reyaz.milliaconnect.data.UserPreferences
import com.reyaz.milliaconnect.ui.screen.VMLogin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    single { UserPreferences(get()) }
    viewModel { VMLogin(get()) }
    single { WorkManager.getInstance(get()) }

}
