package com.reyaz.feature.portal.domain.repository

import com.reyaz.feature.portal.domain.model.ConnectRequest
import org.eclipse.jetty.util.security.Password

interface PortalRepository {
    suspend fun saveCredential(request: ConnectRequest): Result<Unit>
    suspend fun connect(request: ConnectRequest): Result<String>
    suspend fun disconnect(): Result<String>
}

