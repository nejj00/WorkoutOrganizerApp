package com.nejj.workoutorganizerapp.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class ExercisesMainViewModel(
    val workoutRepository: WorkoutRepository
) : ViewModel() {

    fun insertEntity(entity: Exercise) = viewModelScope.launch {
        workoutRepository.upsertExercise(entity)
    }

    fun checkIfExerciseIsUsed(entity: Exercise): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        viewModelScope.launch {
            val routineSetsForExercise = workoutRepository.getRoutineSetsByExerciseId(entity.exerciseId!!)
            result.value = routineSetsForExercise.isNotEmpty()
        }

        return result
    }
    fun deleteEntity(entity: Exercise) = viewModelScope.launch {
        workoutRepository.deleteExercise(entity)
    }

    fun deleteExerciseWithRoutineSets(entity: Exercise) = viewModelScope.launch {
        workoutRepository.deleteRoutineSetsByExerciseId(entity.exerciseId!!)
        workoutRepository.deleteExercise(entity)
    }

    fun getEntities() = workoutRepository.getAllExercises()

    fun getExercisesByCategoryIdLive(categoryId: Long) = workoutRepository.getExercisesByCategoryIdLive(categoryId)

    fun updateExercisesUserUID(userUID: String) = viewModelScope.launch {
        workoutRepository.updateExercisesUserUID(userUID)
    }
}