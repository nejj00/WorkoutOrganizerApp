package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nejj.workoutorganizerapp.models.RoutineSet
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class RoutineSetMainViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app){

    fun insertEntity(entity: RoutineSet) = viewModelScope.launch {
        workoutRepository.upsertRoutineSet(entity)
    }

    fun deleteEntity(entity: RoutineSet) = viewModelScope.launch {
        workoutRepository.deleteRoutineSet(entity)
    }

    fun getEntities() = workoutRepository.getRoutineSets()

    fun getRoutineSetsWithExercise(routineId: Long) = workoutRepository.getRoutineSetsWithExercise(routineId)

    fun insertRoutineSet(routineId: Long, exerciseId: Long) =  viewModelScope.launch {

        val maxOrder = workoutRepository.getRoutineSetMaxOrder(routineId)?: 0
        workoutRepository.upsertRoutineSet(
            RoutineSet(null, routineId, exerciseId, 0, 1, maxOrder + 1)
        )
    }

    fun getRoutineSetsByRoutineId(routineId: Long) = workoutRepository.getRoutineSetsByRoutineId(routineId)
}