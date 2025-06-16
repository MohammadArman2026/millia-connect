package com.reyaz.feature.result.di

import androidx.room.Room
import com.reyaz.feature.result.data.DropdownSelector
import com.reyaz.feature.result.data.ResultRepositoryImpl
import com.reyaz.feature.result.data.ResultScraper
import com.reyaz.feature.result.data.local.ResultDatabase
import com.reyaz.feature.result.domain.repository.ResultRepository
import com.reyaz.feature.result.presentation.ResultViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val resultModule = module {

    single { DropdownSelector() }
    single { ResultScraper(get()) }
    single<ResultRepository> { ResultRepositoryImpl(get(), get()) }
    viewModel { ResultViewModel(get()) }

    single {
        Room.databaseBuilder(
                context = get(),    //get() provides the Context, which Koin resolves from the Application class.
                klass = ResultDatabase::class.java,
                name = ResultDatabase.DB_NAME
            ).fallbackToDestructiveMigration(true).build()
    }

    single { get<ResultDatabase>().resultDao() }
}
