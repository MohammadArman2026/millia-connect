package com.reyaz.core.auth.domain.repository

import android.content.Context
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import kotlinx.coroutines.CoroutineScope

interface GoogleSignIn{
    //for signing function
    fun googleSignIn(
        context: Context,
        scope: CoroutineScope,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
        login:()->Unit
    )
    //for signout from google
    fun googleSignOut()
}