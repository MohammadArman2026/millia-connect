package com.reyaz.core.auth.di

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.reyaz.core.auth.data.repository.GoogleServiceImpl
import com.reyaz.core.auth.domain.repository.GoogleService
import org.koin.dsl.module

val authModule = module{
    single { Firebase.auth }
    single<GoogleService> { GoogleServiceImpl(get()) }
}