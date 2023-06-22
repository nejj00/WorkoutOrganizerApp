package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.models.*
import com.nejj.workoutorganizerapp.repositories.FirestoreRepository
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class LastLoggedInUserViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app) {

    private val _isLoggingInFirstTime = MutableLiveData<Boolean>()
    val isLoggingInFirstTime: LiveData<Boolean>
        get() = _isLoggingInFirstTime

    fun upsertEntity(entity: LastLoggedInUser) = viewModelScope.launch {
        val entites = getLastLoggedInUserList()
        entites.forEach{ deleteEntity(it) }

        workoutRepository.upsertLastLoggedInUser(entity)
    }



    private suspend fun <T> getFirebaseData(userUID: String, collectionPath: String, classToken: Class<T>): MutableList<T> {
        val firestoreRepository = FirestoreRepository(collectionPath)
        val entitiesDocuments = firestoreRepository.getAllEntities()

        val entitiesListFirestore = mutableListOf<T>()
        for(document in entitiesDocuments) {
            entitiesListFirestore.add(document.toObject(classToken)!!)
        }

        return entitiesListFirestore
    }

    private suspend fun <T> insertFirebaseDataToLocalDB(userUID: String, collectionPath: String, classToken: Class<T>, upsertFunction: suspend (T) -> Unit) {
        val entities = getFirebaseData(userUID, collectionPath, classToken)
        entities.forEach { entity ->
            upsertFunction(entity)
        }
    }


    fun updateAllEntitiesUserUID(userUID: String) = viewModelScope.launch {
        val lastLoggedInUser = getLastLoggedInUserList()
        if(lastLoggedInUser.isEmpty()) {
            workoutRepository.batchUpdateUserUIDs(userUID)
        } else {
            if(lastLoggedInUser[0].userUID == userUID) {
                workoutRepository.batchUpdateUserUIDs(userUID)
            } else {
                workoutRepository.nukeDatabase()
                insertFirebaseDataToLocalDB(userUID, "exercise_categories", ExerciseCategory::class.java) { entity ->
                    workoutRepository.upsertCategory(entity)
                }
                insertFirebaseDataToLocalDB(userUID, "exercises", Exercise::class.java) { exercise ->
                    workoutRepository.upsertExercise(exercise)
                }
                insertFirebaseDataToLocalDB(userUID, "workout_routines", WorkoutRoutine::class.java) { workoutRoutine ->
                    workoutRepository.upsertWorkoutRoutine(workoutRoutine)
                }
                insertFirebaseDataToLocalDB(userUID, "routine_sets", RoutineSet::class.java) { routineSet ->
                    workoutRepository.upsertRoutineSet(routineSet)
                }
                insertFirebaseDataToLocalDB(userUID, "logged_workout_routines", LoggedWorkoutRoutine::class.java) { loggedWorkoutRoutine ->
                    workoutRepository.upsertLoggedWorkoutRoutine(loggedWorkoutRoutine)
                }
                insertFirebaseDataToLocalDB(userUID, "logged_routine_sets", LoggedRoutineSet::class.java) { loggedRoutineSet ->
                    workoutRepository.upsertLoggedRoutineSet(loggedRoutineSet)
                }
                insertFirebaseDataToLocalDB(userUID, "logged_exercise_sets", LoggedExerciseSet::class.java) { loggedExerciseSet ->
                    workoutRepository.upsertLoggedExerciseSet(loggedExerciseSet)
                }
            }
        }
    }

    fun deleteEntity(entity: LastLoggedInUser) = viewModelScope.launch {
        workoutRepository.deleteLastLoggedInUser(entity)
    }

    fun getLastLoggedInUser() = workoutRepository.getLastLoggedInUser()

    suspend fun getLastLoggedInUserList() = workoutRepository.getLastLoggedInUserList()
}