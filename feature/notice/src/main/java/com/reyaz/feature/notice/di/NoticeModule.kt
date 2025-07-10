package com.reyaz.feature.notice.di

import androidx.room.Room
import com.reyaz.core.ui.helper.LinkHandler
import com.reyaz.feature.notice.data.NoticeRepository
import com.reyaz.feature.notice.data.local.NoticeDatabase
import com.reyaz.feature.notice.data.remote.NoticeParser
import com.reyaz.feature.notice.data.remote.NoticeScraper
import com.reyaz.feature.notice.domain.usecase.GetNoticeFromNetworkUseCase
import com.reyaz.feature.notice.presentation.NoticeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val noticeModule = module {
    single { NoticeParser() }
    single { LinkHandler(get()) }
    single { NoticeScraper(webClient = get(), parser = get()) }
    single { NoticeRepository(get(), get(), get()) }
    single { GetNoticeFromNetworkUseCase(get(), get(), get()) }
    viewModel { NoticeViewModel(get(), get(),get()) }
    //database
    single {
        Room.databaseBuilder(
            context = get(),    //get() provides the Context, which Koin resolves from the Application class.
            klass = NoticeDatabase::class.java,
            name = NoticeDatabase.DB_NAME
        ).fallbackToDestructiveMigration(true).build()
    }
    single { get<NoticeDatabase>().noticeDao() }
}