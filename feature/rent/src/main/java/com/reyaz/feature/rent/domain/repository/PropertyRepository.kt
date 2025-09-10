package com.reyaz.feature.rent.domain.repository

import com.reyaz.feature.rent.domain.model.Property
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    suspend fun getAllProperty(): Flow<List<Property>>
    suspend fun postProperty(property: Property): Result<Unit>

    suspend fun getPropertyById(id: String):Flow<Property?>
}
//it is an interface containing the only abstract code not actual implementation,
//its implementation i will be doing in data -->repository