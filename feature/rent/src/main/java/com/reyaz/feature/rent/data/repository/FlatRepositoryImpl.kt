package com.reyaz.feature.rent.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.reyaz.feature.rent.domain.model.Flat
import com.reyaz.feature.rent.domain.repository.FlatRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FlatRepositoryImpl (private val db: FirebaseFirestore)
    : FlatRepository {

        companion object {
            private const val FLAT_COLLECTION = "flatCollection"
        }

    override fun getAllFlats(): Flow<List<Flat>> {

            return callbackFlow {
                val listener = db.collection(FLAT_COLLECTION)
                    .addSnapshotListener { snapshot, e ->
                        if (snapshot != null) {
                            val users = snapshot.documents.mapNotNull { doc ->
                                doc.toObject(Flat::class.java)?.
                                copy(
                                    id = doc.id   
                                )
                            }
                            trySend(users)
                        }
                    }
                awaitClose { listener.remove() }
            }
        }


    /*this add user function is used to add a new user to the database.
    * and it is generating id by its own not by manually assigning id.
    * */
    override suspend fun addFlat(flat: Flat) {
            val newDocRef = db.collection(FLAT_COLLECTION).document()
            val userWithId =flat.copy(id = newDocRef.id)
            newDocRef.set(userWithId).await()
    }

}