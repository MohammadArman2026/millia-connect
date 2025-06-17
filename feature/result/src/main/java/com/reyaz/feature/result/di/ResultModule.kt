package com.reyaz.feature.result.di

import androidx.room.Room
import com.reyaz.feature.result.data.ResultRepositoryImpl
import com.reyaz.feature.result.data.local.ResultDatabase
import com.reyaz.feature.result.data.mapper.ResultHtmlParser
import com.reyaz.feature.result.data.scraper.ResultApiService
import com.reyaz.feature.result.domain.repository.ResultRepository
import com.reyaz.feature.result.presentation.ResultViewModel
import org.htmlunit.BrowserVersion
import org.htmlunit.NicelyResynchronizingAjaxController
import org.htmlunit.WaitingRefreshHandler
import org.htmlunit.WebClient
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val resultModule = module {
    viewModel { ResultViewModel(get()) }
    single<ResultRepository> { ResultRepositoryImpl(get(), get()) }

    // scraping
//    single { ResultScraper(get(), get()) }
    single {ResultApiService(get(), get())}
    single {
        WebClient(BrowserVersion.CHROME).apply {
            options.isJavaScriptEnabled = true
            options.isCssEnabled = false
            options.isThrowExceptionOnScriptError = false
            options.isThrowExceptionOnFailingStatusCode = false
            options.isPrintContentOnFailingStatusCode = false
            options.isUseInsecureSSL = true  // This allows insecure SSL
            ajaxController = NicelyResynchronizingAjaxController()
            refreshHandler = WaitingRefreshHandler()
        }
    }
    single { ResultHtmlParser() }

    // database
    single {
        Room.databaseBuilder(
                context = get(),    //get() provides the Context, which Koin resolves from the Application class.
                klass = ResultDatabase::class.java,
                name = ResultDatabase.DB_NAME
            ).fallbackToDestructiveMigration(true).build()
    }
    single { get<ResultDatabase>().resultDao() }
}
