package com.reyaz.feature.portal.domain.repository

import com.reyaz.core.common.utils.Resource
import com.reyaz.feature.portal.data.repository.JmiWifiState
import com.reyaz.feature.portal.domain.model.ConnectRequest
import kotlinx.coroutines.flow.Flow

interface PortalRepository {
    suspend fun saveCredential(request: ConnectRequest): Result<Unit>
    suspend fun connect(request: ConnectRequest): Flow<Resource<String>>
    suspend fun disconnect(): Result<String>
    suspend fun checkConnectionState(): JmiWifiState
}

