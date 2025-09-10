package com.reyaz.feature.rent.presentation.property_post_screen

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.reyaz.core.auth.domain.repository.GoogleService
import com.reyaz.feature.rent.domain.model.Property
import com.reyaz.feature.rent.domain.repository.PropertyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PropertyPostViewModel(
    private val propertyRepository: PropertyRepository,
    private val googleSign: GoogleService
): ViewModel() {
//this variable is for keeping the track whether use is logged in or not
    private val _user = MutableStateFlow(Firebase.auth.currentUser)
    val user = _user.asStateFlow()

    //this viewmodel will be responsible for only posting the property
    fun postProperty(property: Property) {
        viewModelScope.launch {
            propertyRepository
                .postProperty(property)
                .onSuccess {
                    Log.d("success", "success")
                }
                .onFailure {
                    Log.d("error", it.message.toString())
                }
        }
    }

    fun doGoogleSignIn(
        context: Context,
        scope: CoroutineScope,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
        login: () -> Unit
        ){
        googleSign.googleSignIn(
            context = context,
            scope = scope,
            launcher = launcher,
            login = login
        )
    }
}