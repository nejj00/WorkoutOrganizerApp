package com.nejj.workoutorganizerapp.repositories

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
                .whereEqualTo("userUID", userUID)
                .get()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "getAllEntities: ", e)
        }

        return querySnapshot?.documents  ?: mutableListOf<DocumentSnapshot>()
    }

    suspend fun getEntityById(fieldName: String, entityId: Any, userUID: String = ""): MutableList<DocumentSnapshot> {
        var querySnapshot: QuerySnapshot? = null

        try {
            querySnapshot = firestoreDatabaseCollectionRef
                .whereEqualTo(fieldName, entityId)
                .whereEqualTo("userUID", userUID)
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

    suspend fun getLastLoggedInUser(): MutableList<DocumentSnapshot> {
        var querySnapshot: QuerySnapshot? = null

        try {
            querySnapshot = firestoreDatabaseCollectionRef.orderBy("lastLoggedIn", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()
        } catch (e: Exception) {
            Log.e(TAG, "getLastLoggedInUser: ", e)
        }

        return querySnapshot?.documents  ?: mutableListOf<DocumentSnapshot>()
    }
}