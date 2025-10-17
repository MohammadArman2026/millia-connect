package com.reyaz.feature.rent.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.reyaz.feature.rent.data.local.ImageApiService
import com.reyaz.feature.rent.data.repository.ImageRepositoryImpl
import com.reyaz.feature.rent.data.repository.PropertyRepositoryImpl
import com.reyaz.feature.rent.domain.repository.ImageRepository
import com.reyaz.feature.rent.domain.repository.PropertyRepository
import com.reyaz.feature.rent.presentation.property_list_screen.PropertyListViewModel
import com.reyaz.feature.rent.presentation.property_post_screen.CreatePostViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val rentModule = module{
    //provides firebase firestore object-->>because it system doesnot know how to create
    //firebase object so we have to do that
    //as firebase is backend service and 3rd party to this project so we have to do that
    single { FirebaseFirestore.getInstance() }
    single{ FirebaseStorage.getInstance() }
    //binding repository to interface and providing repository dependency
    single<PropertyRepository> { PropertyRepositoryImpl(
        get(),
        get()
    ) }

    // Provide Retrofit instance with explicit type
    //this is builder pattern for retrofit
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://api.imgbb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

   // Provide API Service
    single<ImageApiService> {
        get<Retrofit>().create(ImageApiService::class.java)
    }

    // Provide Repository
    single<ImageRepository> { ImageRepositoryImpl(get()) }

    //providing dependency to viewmodel
    viewModel { PropertyListViewModel(get()) }
    viewModel { CreatePostViewModel(
        get(), get(),get()
    ) }
}

//It doesn’t use code generation or annotations (like Dagger/Hilt).
// Instead, it uses a Kotlin DSL to declare and inject dependencies.