package com.reyaz.core.auth.domain.repository

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import kotlinx.coroutines.CoroutineScope

interface AuthRepository {
    fun signIn(
               context: Context,
               scope: CoroutineScope,
               launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
               login:()-> Unit
              )

    fun signOut(context: Context, onSignedOut: () -> Unit)

}