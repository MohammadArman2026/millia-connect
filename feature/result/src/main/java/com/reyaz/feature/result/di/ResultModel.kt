package com.reyaz.feature.result.di

import com.reyaz.feature.result.data.ResultRepositoryImpl
import com.reyaz.feature.result.data.ResultScraper
import com.reyaz.feature.result.domain.repository.ResultRepository
import com.reyaz.feature.result.presentation.ResultViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val resultModule = module {
    single { ResultScraper() }
    single<ResultRepository> { ResultRepositoryImpl(get()) }
    viewModel { ResultViewModel(get()) }
}
