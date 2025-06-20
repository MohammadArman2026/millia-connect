package com.reyaz.feature.notice.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.reyaz.feature.notice.data.remote.NoticeScraper
import org.koin.androidx.compose.koinViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val noticeModule = module {
    single { NoticeScraper(webClient = get()) }
    viewModel { NoticeViewModel(get()) }
}