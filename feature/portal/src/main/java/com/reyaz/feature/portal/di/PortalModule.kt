package com.reyaz.feature.portal.di

import com.reyaz.core.common.utils.NetworkManager
import com.reyaz.feature.portal.data.PortalScraper
import com.reyaz.feature.portal.data.local.PortalDataStore
import com.reyaz.feature.portal.data.repository.PortalRepositoryImpl
import com.reyaz.feature.portal.domain.repository.PortalRepository
import com.reyaz.feature.portal.presentation.PortalViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val portalModule = module {
    single { NetworkManager(get()) }
    single { PortalDataStore(get()) }
    single{ PortalScraper(get<NetworkManager>(), get()) }
    viewModel{ PortalViewModel(get(), get(), get()) }
    single<PortalRepository> { PortalRepositoryImpl(get(), get()) }

}