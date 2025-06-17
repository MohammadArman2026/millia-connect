package com.reyaz.core.network

import org.koin.dsl.module

val networkModule  = module{
    single { PdfManager(context = get()) }
}