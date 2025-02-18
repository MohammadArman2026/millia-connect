package com.reyaz.wifiautoconnect.di

import androidx.work.WorkManager
import com.reyaz.wifiautoconnect.data.UserPreferences
import com.reyaz.wifiautoconnect.ui.screen.VMLogin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    single { UserPreferences }
    viewModel { VMLogin(/*get()*/) }
    single { WorkManager.getInstance(get()) }

}
