package com.reyaz.feature.portal.data.repository

import com.reyaz.feature.portal.data.PortalScraper
import com.reyaz.feature.portal.data.local.PortalDataStore
import com.reyaz.feature.portal.domain.model.ConnectRequest
import com.reyaz.feature.portal.domain.repository.PortalRepository

class PortalRepositoryImpl(
    private val dataStore: PortalDataStore,
    private val portalScraper: PortalScraper
) : PortalRepository {

    override suspend fun saveCredential(request: ConnectRequest): Result<Unit> {
        return try {
            val result =
                dataStore.saveCredentials(username = request.username, password = request.password, autoConnect = request.autoConnect)
            if (result.isSuccess) {
                Result.success(Unit)
            } else {
                // return Result.failure(result.exceptionOrNull() ?: Exception("Unknown Error"))
                throw Exception(result.exceptionOrNull())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun connect(request: ConnectRequest): Result<String> {
        return try {
            val result =
                portalScraper.performLogin(username = request.username, password = request.password)
            if (result.isSuccess) {
                result
            } else {
                // return Result.failure(result.exceptionOrNull() ?: Exception("Unknown Error"))
                throw Exception(result.exceptionOrNull())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun disconnect(): Result<String> {
        return try {
            val result =
                portalScraper.performLogout()
            if (result.isSuccess) {
                result
            } else {
                // return Result.failure(result.exceptionOrNull() ?: Exception("Unknown Error"))
                throw Exception(result.exceptionOrNull())
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

