package com.reyaz.core.auth.data.repository

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.reyaz.core.auth.domain.repository.GoogleService
import com.reyaz.core.auth.domain.repository.WebClientId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GoogleServiceImpl(private val firebaseAuth: FirebaseAuth): GoogleService {
    override fun googleSignIn(
        context: Context,
        scope: CoroutineScope,
        launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
        login: () -> Unit
    ) {

        val credentialManager = CredentialManager.create(context)//credential manager it holds the credential

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(getCredentialOption())
            .build()

        scope.launch {   //scope is passed from ui
            try {
                val result = credentialManager.getCredential(context, request)
                when (result.credential) {
                    is CustomCredential -> {
                        if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(result.credential.data)
                            val googleTokenId = googleIdTokenCredential.idToken
                            val authCredential =
                                GoogleAuthProvider.getCredential(googleTokenId, null)
                            val user =
                                firebaseAuth.signInWithCredential(authCredential).await().user
                            user?.let {
                                if (it.isAnonymous.not()) {
                                    login.invoke()
                                    Log.d("login","logged in ")
                                }
                            }
                        }
                    }
                    else -> {

                    }
                }
            } catch (e: NoCredentialException) {
                launcher?.launch(getIntent())
            } catch (e: GetCredentialException) {
                e.printStackTrace()
            }
        }

    }

    private fun getIntent(): Intent {
        return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
            putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
        }
    }

    private fun getCredentialOption(): CredentialOption {
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .setServerClientId(WebClientId.API_KEY)
            .build()
    }

    //pass context from user interface ,now everything is ready
    override fun googleSignOut(context: Context) {
        // 1. Firebase
        firebaseAuth.signOut()
        // 2. Credential Manager (clear saved OAuth credential)
        val credentialManager = CredentialManager.create(context)
        val clearRequest = ClearCredentialStateRequest()

        CoroutineScope(Dispatchers.Main).launch {
            try {
                credentialManager.clearCredentialState(clearRequest)
                Log.d("Auth", "Signed out successfully")
            } catch (e: Exception) {
                Log.e("Auth", "Error clearing credentials", e)
            }
        }
    }
}