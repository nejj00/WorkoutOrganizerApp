package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.nejj.workoutorganizerapp.models.CategoriesResponse
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

class ExercisesMainViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app) {

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

    fun getEntities() = workoutRepository.getExercises()

    fun getExercisesByCategoryIdLive(categoryId: Long) = workoutRepository.getExercisesByCategoryIdLive(categoryId)

    fun updateExercisesUserUID(userUID: String) = viewModelScope.launch {
        workoutRepository.updateExercisesUserUID(userUID)
    }
}