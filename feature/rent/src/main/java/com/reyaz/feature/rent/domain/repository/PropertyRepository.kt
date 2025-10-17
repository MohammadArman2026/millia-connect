package com.reyaz.feature.rent.domain.repository

import android.net.Uri
import com.reyaz.feature.rent.domain.model.Property
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    //this will fetch all property
    suspend fun getAllProperty(): Flow<List<Property>>
    suspend fun postProperty(property: Property): Result<Unit>
    suspend fun getPropertyById(id: String):Flow<Property?>

    suspend fun getPropertyByFilter(text:String):Flow<List<Property>>?

//    suspend fun getUrl(uris:List<Uri>,onSuccess:(List<String>)->Unit,onError:(Exception)->Unit)

//    suspend fun deleteProperty(id:String):Result<Unit>
}
//it is an interface containing the only abstract code not actual implementation,
//its implementation i will be doing in data -->repository