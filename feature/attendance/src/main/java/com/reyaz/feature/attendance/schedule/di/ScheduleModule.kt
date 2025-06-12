package com.reyaz.feature.attendance.schedule.di

import com.reyaz.feature.attendance.schedule.presentation.ScheduleViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val scheduleModule = module {
//    single<AuthRepository> { AuthRepositoryImpl() } // or however you're providing it
    viewModel { ScheduleViewModel() }
}