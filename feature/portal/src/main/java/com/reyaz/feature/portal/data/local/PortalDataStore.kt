package com.reyaz.feature.portal.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class PortalDataStore(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "portal_datastore")

        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
        val LOGIN_STATUS = booleanPreferencesKey("status")
        val AUTO_CONNECT = booleanPreferencesKey("auto_connect")
    }

    // Get saved username
    val username: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[USERNAME]
        }

    // Get saved password
    val password: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[PASSWORD]
        }

    // Get Auto Connect Status
    val autoConnect: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[AUTO_CONNECT] ?: true
        }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map {
        it[LOGIN_STATUS] ?: false
    }

    // Save credentials
    suspend fun saveCredentials(
        username: String,
        password: String,
        autoConnect: Boolean
    ) : Result<Unit>{
        return try {
            context.dataStore.edit { preferences ->
                preferences[USERNAME] = username
                preferences[PASSWORD] = password
                preferences[AUTO_CONNECT] = autoConnect
            }
            Result.success(Unit)
        } catch (e:Exception){
             Result.failure(e)
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

    suspend fun setLoggedIn(isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[LOGIN_STATUS] = isLoggedIn
        }
    }
}