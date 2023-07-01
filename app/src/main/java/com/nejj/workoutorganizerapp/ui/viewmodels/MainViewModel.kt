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


}