package com.reyaz.milliaconnect1.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferences(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
        val LOGIN_STATUS = booleanPreferencesKey("status")
        val AUTO_CONNECT = booleanPreferencesKey("auto_connect")
    }

    // Get saved username
    val username: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[USERNAME] ?: ""
        }

    // Get saved password
    val password: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PASSWORD] ?: ""
        }

    val loginStatus: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[LOGIN_STATUS] ?: false
        }

    // Get Auto Connect Status
    val autoConnect: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[AUTO_CONNECT] ?: true
        }



    // Save credentials
    suspend fun saveCredentials(
        username: String,
        password: String,
        isLoggedIn: Boolean,
        autoConnect: Boolean
    ) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME] = username
            preferences[PASSWORD] = password
            preferences[LOGIN_STATUS] = isLoggedIn
            preferences[AUTO_CONNECT] = autoConnect
        }
    }

    // Clear credentials
    suspend fun clearCredentials() {
        context.dataStore.edit { preferences ->
            preferences.remove(USERNAME)
            preferences.remove(PASSWORD)
        }
    }

    suspend fun setAutoConnect(autoConnect: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_CONNECT] = autoConnect
        }
    }
    suspend fun setLoginStatus(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LOGIN_STATUS] = isLoggedIn
        }
    }
}