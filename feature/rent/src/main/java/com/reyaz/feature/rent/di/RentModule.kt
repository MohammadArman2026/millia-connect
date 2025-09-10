package com.reyaz.feature.rent.di

import com.google.firebase.firestore.FirebaseFirestore
import com.reyaz.feature.rent.data.repository.PropertyRepositoryImpl
import com.reyaz.feature.rent.domain.repository.PropertyRepository
import com.reyaz.feature.rent.presentation.property_list_screen.PropertyListViewModel
import com.reyaz.feature.rent.presentation.property_post_screen.PropertyPostViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val rentModule = module{
    //provides firebase firestore object-->>because it system doesnot know how to create
    //firebase object so we have to do that
    //as firebase is backend service and 3rd party to this project so we have to do that
    single { FirebaseFirestore.getInstance() }
    //binding repository to interface and providing repository dependency
    single<PropertyRepository> { PropertyRepositoryImpl(get()) }

    //providing dependency to viewmodel
    viewModel { PropertyListViewModel(get()) }
    viewModel { PropertyPostViewModel(get(),get()) }
}

//It doesn’t use code generation or annotations (like Dagger/Hilt).
// Instead, it uses a Kotlin DSL to declare and inject dependencies.