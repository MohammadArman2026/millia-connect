package com.reyaz.feature.rent.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.reyaz.feature.rent.domain.model.Property
import com.reyaz.feature.rent.domain.repository.PropertyRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PropertyRepositoryImpl(
    private val firebaseFirestore: FirebaseFirestore
): PropertyRepository {


    private val Property = "PROPERTY"//this is the name of collection at firebase

    //here basically creating a collection at firebase and this property collection is
    //pointing at the collection name "property"
    private val propertyCollection by lazy {
        firebaseFirestore.collection(Property)
    }

    override suspend fun getAllProperty(): Flow<List<Property>> {
        return callbackFlow {

            propertyCollection.addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val list = snapshot?.toObjects<Property>()
                    .orEmpty()//it is converting data from firebase to our property class
                trySend(list)//this line is basically sending the list
            }
            awaitClose {}
        }
    }

    //this function will post the property in firebase,and will save the id field same as firebase generated id
    override suspend fun postProperty(property: Property): Result<Unit> {
        return try {
            val docRef = propertyCollection.document()
            val propertyWithId = property.copy(id = docRef.id)
            docRef.set(propertyWithId).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun getPropertyById(id: String): Flow<Property?> {
        return callbackFlow {
           val registration = propertyCollection.document(id)
                .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val property = snapshot?.toObject<Property>()
                trySend(property)
            }
            awaitClose { registration.remove() }//cleanup listeners properly
        }
    }
}

//here i have used call back flow because firebase listener is call back listener base api
//the philosophy behind call back is same as restaurant -- where u order something and when it ready they will call u in the same way
//this callback flow works

