package com.reyaz.core.network.utils

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private const val DATASTORE_NAME = "network_request_time"

val Context.requestTimeDataStore by preferencesDataStore(name = DATASTORE_NAME)

class RequestTimeStore(
    private val context: Context
) {
    private fun getKey(typeId: String): Preferences.Key<Long> = longPreferencesKey("last_fetched_$typeId")

    suspend fun saveRequestTime(timeStamp: Long, typeId: String) {
        context.requestTimeDataStore.edit { preferences ->
            preferences[getKey(typeId)] = timeStamp
        }
    }

    private suspend fun getLastFetchedTime(typeId: String): Long?{
        val prefs = context.requestTimeDataStore.data.first()
        return prefs[getKey(typeId)]
    }

    suspend fun shouldRefresh(typeId: String, thresholdMillis: Long = 60 * 60 * 1000): Boolean {
        val lastTime = getLastFetchedTime(typeId)
        val currentTime = System.currentTimeMillis()
        return lastTime == null || currentTime - lastTime > thresholdMillis
    }
}