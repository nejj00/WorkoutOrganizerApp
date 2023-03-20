package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class CategoriesMainViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app) {

    fun insertCategory(exerciseCategory: ExerciseCategory) = viewModelScope.launch {
        workoutRepository.upsert(exerciseCategory)
    }

    fun getCategories() = workoutRepository.getCategories()

    fun deleteCategory(exerciseCategory: ExerciseCategory) = viewModelScope.launch {
        workoutRepository.deleteCategory(exerciseCategory)
    }
}