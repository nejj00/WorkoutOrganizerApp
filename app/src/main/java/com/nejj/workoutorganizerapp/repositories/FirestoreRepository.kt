package com.nejj.workoutorganizerapp.repositories

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.database.firestore.FirestoreDatabase
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FirestoreRepository (
    private val collectionPath: String
    ) {

    companion object {
        private const val TAG = "FirestoreRepository"
    }

    private val firestoreDatabaseCollectionRef = Firebase.firestore.collection(collectionPath)

    suspend fun getAllEntities(userUID: String = "") : MutableList<DocumentSnapshot> {
        var querySnapshot: QuerySnapshot? = null

        try {
            querySnapshot = firestoreDatabaseCollectionRef
                //.whereEqualTo("userUID", userUID)
                .get()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "getAllEntities: ", e)
        }

        return querySnapshot?.documents  ?: mutableListOf<DocumentSnapshot>()
    }

    suspend fun getEntityById(fieldName: String, entityId: Long, userUID: String = ""): MutableList<DocumentSnapshot> {
        var querySnapshot: QuerySnapshot? = null

        try {
            querySnapshot = firestoreDatabaseCollectionRef
                .whereEqualTo(fieldName, entityId)
                //.whereEqualTo("userUID", userUID)
                .get()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "getEntityById: ", e)
        }


        return querySnapshot?.documents  ?: mutableListOf<DocumentSnapshot>()
    }

    suspend fun deleteEntity(documentId: String) {
        try {
            firestoreDatabaseCollectionRef.document(documentId).delete().await()
        } catch (e: Exception) {
            Log.e(TAG, "deleteEntity: ", e)
        }
    }

    suspend fun updateEntity(documentId: String, newEntityMap: Map<String, Any>) {
        try {
            firestoreDatabaseCollectionRef.document(documentId).set(
                newEntityMap,
                SetOptions.merge()
            ).await()
        } catch (e: Exception) {
            Log.e(TAG, "updateEntity: ", e)
        }
    }

    suspend fun insertEntity(entityMap: Map<String, Any>) {
        try {
            firestoreDatabaseCollectionRef.add(
                entityMap
            ).await()
        } catch (e: Exception) {
            Log.e(TAG, "insertEntity: ", e)
        }
    }

//    fun saveExerciseCategories() = CoroutineScope(Dispatchers.IO).launch {
//        val exerciseCategoriesCollectionRef = Firebase.firestore.collection("exercise_categories")
//
//        val exerciseCategoriesSnapshot = exerciseCategoriesCollectionRef
//            //.whereEqualTo("userUID", Firebase.auth.currentUser!!.uid)
//            .get()
//            .await()
//
//        val exerciseCategoriesFirestore = mutableListOf<ExerciseCategory>()
//        for(document in exerciseCategoriesSnapshot.documents) {
//            exerciseCategoriesFirestore.add(document.toObject(ExerciseCategory::class.java)!!)
//        }
//
//        val exerciseCategoriesLocal = workoutRepository.getCategoriesList()
//
//        val entitiesToInsertAndUpdate = exerciseCategoriesLocal.toSet().minus(exerciseCategoriesFirestore.toSet())
//        val entitiesToDelete = exerciseCategoriesFirestore.toSet().minus(exerciseCategoriesLocal.toSet())
//
//        for(exerciseCategory in entitiesToDelete) {
//            val entityToDelete = exerciseCategoriesCollectionRef
//                .whereEqualTo("categoryId", exerciseCategory.categoryId)
//                //.whereEqualTo("userUID", exerciseCategory.userUID)
//                .get()
//                .await()
//
//            for (document in entityToDelete) {
//                exerciseCategoriesCollectionRef.document(document.id).delete().await()
//            }
//        }
//
//        for(exerciseCategory in entitiesToInsertAndUpdate) {
//            val entityToUpdate = exerciseCategoriesCollectionRef
//                .whereEqualTo("categoryId", exerciseCategory.categoryId)
//                //.whereEqualTo("userUID", exerciseCategory.userUID)
//                .get()
//                .await()
//
//            if(entityToUpdate.documents.isNotEmpty()) {
//                val map = mutableMapOf<String, Any>()
//                map["categoryId"] = exerciseCategory.categoryId!!
//                map["name"] = exerciseCategory.name
//                map["isUserMade"] = exerciseCategory.isUserMade
//
//                for (document in entityToUpdate) {
//                    exerciseCategoriesCollectionRef.document(document.id).set(
//                        map,
//                        SetOptions.merge()
//                    ).addOnSuccessListener {
//                        Log.e(TAG,"DocumentSnapshot updated with ID: ${document.id}")
//                    }
//                    .addOnFailureListener() { e ->
//                        Log.e(TAG, "Error updating document", e)
//                    }
//                }
//
//            } else {
//                exerciseCategoriesCollectionRef
//                    .add(exerciseCategory)
//                    .addOnSuccessListener { documentReference ->
//                        Log.e(TAG,"DocumentSnapshot added with ID: ${documentReference.id}")
//                    }
//                    .addOnFailureListener() { e ->
//                        Log.e(TAG, "Error adding document", e)
//                    }
//            }
//        }
//    }
//
//    fun deleteExerciseCategories() = CoroutineScope(Dispatchers.IO).launch {
//        val exerciseCategoriesCollectionRef = Firebase.firestore.collection("exercise_categories")
//        exerciseCategoriesCollectionRef
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    exerciseCategoriesCollectionRef.document(document.id).delete()
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.e(TAG, "Error getting documents: ", exception)
//            }
//            .await()
//    }
}