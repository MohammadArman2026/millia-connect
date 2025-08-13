package com.reyaz.feature.rent.di

import com.reyaz.feature.rent.data.repository.FlatRepositoryImpl
import com.reyaz.feature.rent.domain.repository.FlatRepository
import com.reyaz.feature.rent.presentation.flatscreen.FlatScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val rentModule = module {
    single <FlatRepository>{ FlatRepositoryImpl(get()) }
    viewModel { FlatScreenViewModel(get()) }
}