package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nejj.workoutorganizerapp.models.CategoriesResponse
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

class ExercisesMainViewModel(
    app: Application,
    workoutRepository: WorkoutRepository
) : MainViewModel<Exercise, CategoriesResponse> (app, workoutRepository) {

    override fun insertEntity(entity: Exercise) = viewModelScope.launch {
        workoutRepository.upsertExercise(entity)
    }

    override fun deleteEntity(entity: Exercise) = viewModelScope.launch {
        workoutRepository.deleteExercise(entity)
    }

    override fun getEntities() = workoutRepository.getExercises()

    override suspend fun getEntitiesApi(): Response<CategoriesResponse> {
        TODO("Not yet implemented")
    }
}