package com.nejj.workoutorganizerapp.database.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreDatabase {

    fun getExerciseCategoryCollectionReference() = Firebase.firestore.collection("exercise_categories")
}