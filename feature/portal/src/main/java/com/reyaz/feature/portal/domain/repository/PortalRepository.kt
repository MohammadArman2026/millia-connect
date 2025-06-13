package com.reyaz.feature.portal.domain.repository

import com.reyaz.feature.portal.data.repository.JmiWifiState
import com.reyaz.feature.portal.domain.model.ConnectRequest

interface PortalRepository {
    suspend fun saveCredential(request: ConnectRequest): Result<Unit>
    suspend fun connect(request: ConnectRequest): Result<String>
    suspend fun disconnect(): Result<String>
    suspend fun checkConnectionState(): JmiWifiState
    suspend fun isWifiPrimary(): Boolean
}

