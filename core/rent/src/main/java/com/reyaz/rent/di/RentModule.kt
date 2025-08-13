package com.reyaz.rent.di

import com.reyaz.rent.data.repository.FlatRepositoryImpl
import com.reyaz.rent.domain.repository.FlatRepository
import com.reyaz.rent.presentation.flatscreen.FlatScreenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val rentModule = module {

    single <FlatRepository>{ FlatRepositoryImpl(get()) }
    viewModel { FlatScreenViewModel(get()) }
}