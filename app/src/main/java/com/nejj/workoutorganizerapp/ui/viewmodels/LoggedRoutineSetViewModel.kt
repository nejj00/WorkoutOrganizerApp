package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.models.relations.LoggedRoutineSetWithLoggedExerciseSet
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class LoggedRoutineSetViewModel(
    val workoutRepository: WorkoutRepository
) : ViewModel() {

    fun insertEntity(entity: LoggedRoutineSet) = viewModelScope.launch {
        workoutRepository.upsertLoggedRoutineSet(entity)
    }

    fun deleteEntity(entity: LoggedRoutineSetWithLoggedExerciseSet) = viewModelScope.launch {
        entity.loggedExerciseSets.forEach { loggedExerciseSet ->
            workoutRepository.deleteLoggedExerciseSet(loggedExerciseSet)
        }

        workoutRepository.deleteLoggedRoutineSet(entity.loggedRoutineSet)
    }

    fun updateLoggedRoutineSetsUserUID(userUID: String) = viewModelScope.launch {
        workoutRepository.updateLoggedRoutineSetsUserUID(userUID)
    }

    fun insertNewLoggedRoutineSet(loggedRoutineId: Long, exercise: Exercise) = viewModelScope.launch {
        val maxOrder = workoutRepository.getLoggedRouitneSetMaxOrder(loggedRoutineId)?: 0

        val loggedRoutineSet = LoggedRoutineSet()
        loggedRoutineSet.loggedRoutineId = loggedRoutineId
        loggedRoutineSet.exerciseId = exercise.exerciseId
        loggedRoutineSet.exerciseName = exercise.name
        loggedRoutineSet.setsOrder = maxOrder + 1
        loggedRoutineSet.setsCount = 1
        loggedRoutineSet.userUID = Firebase.auth.currentUser?.uid

        val loggedRoutineSetId = workoutRepository.upsertLoggedRoutineSet(loggedRoutineSet)

        val loggedExerciseSet = LoggedExerciseSet()
        loggedExerciseSet.loggedRoutineId = loggedRoutineId
        loggedExerciseSet.loggedRoutineSetId = loggedRoutineSetId
        loggedExerciseSet.userUID = Firebase.auth.currentUser?.uid

        workoutRepository.upsertLoggedExerciseSet(loggedExerciseSet)
    }

    fun getLoggedRoutineSetsWithLoggedExerciseSets(loggedRoutineId: Long) = workoutRepository.getLoggedRoutineSetsWithLoggedExerciseSets(loggedRoutineId)

    suspend fun getLoggedRoutineSetsByLoggedRoutineId(loggedRoutineId: Long) = workoutRepository.getLoggedRoutineSetsByRoutineId(loggedRoutineId)
}