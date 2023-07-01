package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.toObject
import com.nejj.workoutorganizerapp.models.CategoriesResponse
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.repositories.DatabaseSynchronizer
import com.nejj.workoutorganizerapp.repositories.FirestoreRepository
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesMainViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app) {

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
    fun getEntities() = workoutRepository.getCategories()
    fun updateCategoriesUserUID(userUID: String) = viewModelScope.launch {
        workoutRepository.updateCategoriesUserUID(userUID)
    }
    suspend fun getEntitiesApi() = workoutRepository.getCategoriesApi()
}