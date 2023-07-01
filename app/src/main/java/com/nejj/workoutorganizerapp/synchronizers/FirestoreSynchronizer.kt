package com.nejj.workoutorganizerapp.synchronizers

import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.repositories.FirestoreRepository
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class FirestoreSynchronizer<T : Any>(
    val workoutRepository: WorkoutRepository,
    collectionPath: String,
) {

    abstract suspend fun getLocalEntitiesList(): List<T>
    abstract fun getEntityId(entity: T): Long
    abstract fun getIdFieldName(): String
    abstract fun getMapFromEntity(entity: T): Map<String, Any>
    abstract fun insertToFirestoreList(document: DocumentSnapshot, entitiesListFirestore: MutableList<T>)
    abstract suspend fun upsertdDataToLocalDB(entity: T)

    private val firestoreRepository = FirestoreRepository(collectionPath)

    fun saveEntitiesToFirebase() = CoroutineScope(Dispatchers.IO).launch {
        val entitiesDocuments = firestoreRepository.getAllEntities()

        val entitiesListFirestore = mutableListOf<T>()
        for(document in entitiesDocuments) {
            insertToFirestoreList(document, entitiesListFirestore)
        }

        val entitiesListLocal = getLocalEntitiesList()

        val entitiesToInsertAndUpdate = entitiesListLocal.toSet().minus(entitiesListFirestore.toSet())
        val entitiesToDelete = entitiesListFirestore.toSet().minus(entitiesListLocal.toSet())

        for(entity in entitiesToDelete) {
            val entityToDelete = firestoreRepository.getEntityById(getIdFieldName(), getEntityId(entity), Firebase.auth.currentUser!!.uid)

            for (document in entityToDelete) {
                firestoreRepository.deleteEntity(document.id)
            }
        }

        for(entity in entitiesToInsertAndUpdate) {
            val entityToUpdate = firestoreRepository.getEntityById(getIdFieldName(), getEntityId(entity))
            val map = getMapFromEntity(entity)
            if(entityToUpdate.isNotEmpty()) {
                for (document in entityToUpdate) {
                    firestoreRepository.updateEntity(document.id, map)
                }
            } else {
                firestoreRepository.insertEntity(map)
            }
        }
    }

    fun deleteEntitiesFirebase() = CoroutineScope(Dispatchers.IO).launch {
        val entitiesDocuments = firestoreRepository.getAllEntities()
        for(document in entitiesDocuments) {
            firestoreRepository.deleteEntity(document.id)
        }
    }

    private suspend fun getFirebaseData(userUID: String): MutableList<T> {
        val entitiesDocuments = firestoreRepository.getAllEntities(userUID)

        val entitiesListFirestore = mutableListOf<T>()
        for(document in entitiesDocuments) {
            insertToFirestoreList(document, entitiesListFirestore)
        }

        return entitiesListFirestore
    }

    suspend fun insertFirebaseDataToLocalDB(userUID: String) {
        val entities = getFirebaseData(userUID)
        entities.forEach { entity ->
            upsertdDataToLocalDB(entity)
        }
    }
}