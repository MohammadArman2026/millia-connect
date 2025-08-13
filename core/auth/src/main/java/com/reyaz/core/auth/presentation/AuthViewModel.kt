package com.reyaz.core.auth.presentation

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.reyaz.core.auth.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(
    private val authRepository: AuthRepository
){
    private val _email = MutableStateFlow(Firebase.auth.currentUser?.email ?: "")
    val email=_email.asStateFlow()
    private val _name = MutableStateFlow(Firebase.auth.currentUser?.displayName ?: "")
    val name=_name.asStateFlow()

    fun signIn(
        context: Context,
        scope: CoroutineScope,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
        login: () -> Unit
    ){
        authRepository.signIn(
            context =context,
            scope = scope,
            launcher = launcher,
            login = login
        )
    }

    fun signOut(
        context: Context,
        onSignedOut: () -> Unit
    ){
        authRepository.signOut(
            context = context,
            onSignedOut = onSignedOut
        )
    }
}