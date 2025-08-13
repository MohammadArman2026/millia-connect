package com.reyaz.rent.domain.repository

import com.reyaz.rent.domain.model.Flat
import kotlinx.coroutines.flow.Flow

interface FlatRepository {
    fun getAllFlats(): Flow<List<Flat>>
    suspend fun addFlat(flat: Flat)
}
