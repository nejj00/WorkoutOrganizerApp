package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class CategoriesMainViewModel(
    val workoutRepository: WorkoutRepository
) : ViewModel() {

//    init {
//        val databaseSynchronizer = DatabaseSynchronizer()
//        viewModelScope.launch {
//            databaseSynchronizer.updateLocalDatabase<ExerciseCategory, CategoriesResponse>(
//                getEntitiesApi(),
//                { categoriesResponse -> categoriesResponse.categories },
//                getEntities(),
//                { category -> insertEntity(category) }
//                )
//        }
//    }
    fun insertEntity(entity: ExerciseCategory) = viewModelScope.launch {
            workoutRepository.upsertCategory(entity)
    }
    fun checkIfCategoryIsUsed(entity: ExerciseCategory): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        viewModelScope.launch {
            val exercisesForCategory = workoutRepository.getExercisesByCategoryId(entity.categoryId!!)
            result.value = exercisesForCategory.isNotEmpty()
        }

        return result
    }
    fun deleteEntity(entity: ExerciseCategory) = viewModelScope.launch {
        workoutRepository.deleteCategory(entity)
    }
    fun getEntities() = workoutRepository.getAllCategories()
    fun updateCategoriesUserUID(userUID: String) = viewModelScope.launch {
        workoutRepository.updateCategoriesUserUID(userUID)
    }
    suspend fun getEntitiesApi() = workoutRepository.getCategoriesApi()
}