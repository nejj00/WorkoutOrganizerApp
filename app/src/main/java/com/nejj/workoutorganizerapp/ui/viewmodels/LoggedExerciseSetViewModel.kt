package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class LoggedExerciseSetViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app
) {

    fun insertEntity(entity: LoggedExerciseSet) = viewModelScope.launch {
        workoutRepository.upsertLoggedExerciseSet(entity)
    }

    fun deleteEntity(entity: LoggedExerciseSet) = viewModelScope.launch {
        workoutRepository.deleteLoggedExerciseSet(entity)
    }

    fun insertNewExerciseSetToLoggedRoutineSet(loggedRoutineSet: LoggedRoutineSet) = viewModelScope.launch {
        val maxOrder = workoutRepository.getLoggedExerciseSetMaxOrder(loggedRoutineSet.loggedRoutineSetId!!)?: 0

        val loggedExerciseSet = LoggedExerciseSet()
        loggedExerciseSet.loggedRoutineId = loggedRoutineSet.loggedRoutineId
        loggedExerciseSet.loggedRoutineSetId = loggedRoutineSet.loggedRoutineSetId
        loggedExerciseSet.setOrder = maxOrder + 1

        workoutRepository.upsertLoggedExerciseSet(loggedExerciseSet)
    }
}