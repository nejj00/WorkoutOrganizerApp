package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.nejj.workoutorganizerapp.models.CategoriesResponse
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.repositories.DatabaseSynchronizer
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class CategoriesMainViewModel(
    app: Application,
    workoutRepository: WorkoutRepository
) : MainViewModel<ExerciseCategory, CategoriesResponse>(app, workoutRepository) {

    init {
//        val databaseSynchronizer = DatabaseSynchronizer()
//        viewModelScope.launch {
//            databaseSynchronizer.updateLocalDatabase<ExerciseCategory, CategoriesResponse>(
//                getEntitiesApi(),
//                { categoriesResponse -> categoriesResponse.categories },
//                getEntities(),
//                { category -> insertEntity(category) }
//                )
//        }
    }
    override fun insertEntity(entity: ExerciseCategory) = viewModelScope.launch {
            workoutRepository.upsertCategory(entity)
    }
    override fun deleteEntity(entity: ExerciseCategory) = viewModelScope.launch {
        workoutRepository.deleteCategory(entity)
    }
    override fun getEntities() = workoutRepository.getCategories()
    override suspend fun getEntitiesApi() = workoutRepository.getCategoriesApi()
}