package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.nejj.workoutorganizerapp.repositories.FirestoreRepository
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class MainViewModel<T : Any>(
    app: Application,
    val workoutRepository: WorkoutRepository,
    collectionPath: String,
) : AndroidViewModel(app) {

    protected abstract val classToken: Class<T>
    abstract suspend fun getLocalEntitiesList(): List<T>
    abstract fun getEntityId(entity: T): Long
    abstract fun getIdFieldName(): String
    abstract fun getMapFromEntity(entity: T): Map<String, Any>

    abstract fun insertToFirestoreList(document: DocumentSnapshot, entitiesListFirestore: MutableList<T>)

    private val firestoreRepository = FirestoreRepository(collectionPath)

    fun saveExerciseCategoriesFirebase() = CoroutineScope(Dispatchers.IO).launch {
        val entitiesDocuments = firestoreRepository.getAllEntities()

        val entitiesListFirestore = mutableListOf<T>()
        for(document in entitiesDocuments) {
            //entitiesListFirestore.add(document.toObject(classToken)!!)
            insertToFirestoreList(document, entitiesListFirestore)
        }

        val entitiesListLocal = getLocalEntitiesList()

        val entitiesToInsertAndUpdate = entitiesListLocal.toSet().minus(entitiesListFirestore.toSet())
        val entitiesToDelete = entitiesListFirestore.toSet().minus(entitiesListLocal.toSet())

        for(entity in entitiesToDelete) {
            val entityToDelete = firestoreRepository.getEntityById(getIdFieldName(), getEntityId(entity as T))

            for (document in entityToDelete) {
                firestoreRepository.deleteEntity(document.id)
            }
        }

        for(entity in entitiesToInsertAndUpdate) {
            val entityToUpdate = firestoreRepository.getEntityById(getIdFieldName(), getEntityId(entity as T))
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
}