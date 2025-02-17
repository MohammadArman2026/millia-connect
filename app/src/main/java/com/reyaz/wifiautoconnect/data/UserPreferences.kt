package com.reyaz.wifiautoconnect.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
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
        val BASE_URL = stringPreferencesKey("base_url")
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
    val baseUrl: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[BASE_URL] ?: "http://10.2.0.10:8090/login?"
        }

    // Save credentials
    suspend fun saveCredentials(username: String, password: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME] = username
            preferences[PASSWORD] = password
        }
    }

    // Clear credentials
    suspend fun clearCredentials() {
        context.dataStore.edit { preferences ->
            preferences.remove(USERNAME)
            preferences.remove(PASSWORD)
        }
    }
}