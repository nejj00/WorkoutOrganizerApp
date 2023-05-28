package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.models.relations.LoggedRoutineSetWithLoggedExerciseSet
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class LoggedRoutineSetViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app) {

    fun insertEntity(entity: LoggedRoutineSet) = viewModelScope.launch {
        workoutRepository.upsertLoggedRoutineSet(entity)
    }

    fun deleteEntity(entity: LoggedRoutineSetWithLoggedExerciseSet) = viewModelScope.launch {
        entity.loggedExerciseSets.forEach { loggedExerciseSet ->
            workoutRepository.deleteLoggedExerciseSet(loggedExerciseSet)
        }

        workoutRepository.deleteLoggedRoutineSet(entity.loggedRoutineSet)
    }

    fun insertNewLoggedRoutineSet(loggedRoutineId: Long, exercise: Exercise) = viewModelScope.launch {
        val maxOrder = workoutRepository.getLoggedRouitneSetMaxOrder(loggedRoutineId)?: 0

        val loggedRoutineSet = LoggedRoutineSet()
        loggedRoutineSet.loggedRoutineId = loggedRoutineId
        loggedRoutineSet.exerciseId = exercise.exerciseId
        loggedRoutineSet.exerciseName = exercise.name
        loggedRoutineSet.setsOrder = maxOrder + 1
        loggedRoutineSet.setsCount = 1

        val loggedRoutineSetId = workoutRepository.upsertLoggedRoutineSet(loggedRoutineSet)

        val loggedExerciseSet = LoggedExerciseSet()
        loggedExerciseSet.loggedRoutineId = loggedRoutineId
        loggedExerciseSet.loggedRoutineSetId = loggedRoutineSetId
        workoutRepository.upsertLoggedExerciseSet(loggedExerciseSet)
    }

    fun getLoggedRoutineSetsWithLoggedExerciseSets(loggedRoutineId: Long) = workoutRepository.getLoggedRoutineSetsWithLoggedExerciseSets(loggedRoutineId)
}