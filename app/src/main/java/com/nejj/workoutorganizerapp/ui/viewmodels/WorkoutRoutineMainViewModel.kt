package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nejj.workoutorganizerapp.models.WorkoutRoutine
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutRoutineMainViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app) {

    fun insertEntity(entity: WorkoutRoutine) = viewModelScope.launch {
        workoutRepository.upsertWorkoutRoutine(entity)
    }

    fun deleteEntity(entity: WorkoutRoutine) = viewModelScope.launch {
        workoutRepository.deleteWorkoutRoutine(entity)
    }

    fun getEntities() = workoutRepository.getWorkoutRoutines()

    fun getWorkoutRoutineWithRoutineSets(routineId: Long) = workoutRepository.getWorkoutRoutineWithRoutineSets(routineId)

    fun getWorkoutRoutineWithExercises(routineId: Long) = workoutRepository.getWorkoutRoutineWithExercises(routineId)
}