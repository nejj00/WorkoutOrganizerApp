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
    workoutRepository: WorkoutRepository
) : MainViewModel<ExerciseCategory>(app, workoutRepository, "exercise_categories") {

    override val classToken: Class<ExerciseCategory>
        get() = ExerciseCategory::class.java

    override suspend fun getLocalEntitiesList(): List<ExerciseCategory> {
        return workoutRepository.getUserMadeCategoriesList()
    }

    override fun getIdFieldName(): String {
        return "categoryId"
    }

    override fun insertToFirestoreList(
        document: DocumentSnapshot,
        entitiesListFirestore: MutableList<ExerciseCategory>
    ) {
        entitiesListFirestore.add(document.toObject(classToken)!!)
    }

    override fun getMapFromEntity(entity: ExerciseCategory): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["categoryId"] = entity.categoryId!!
        map["name"] = entity.name
        map["isUserMade"] = entity.isUserMade
        map["userUID"] = entity.userUID ?: ""

        return map
    }

    override fun getEntityId(entity: ExerciseCategory): Long {
        return entity.categoryId!!
    }

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
    fun deleteEntity(entity: ExerciseCategory): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        viewModelScope.launch {
            val exercisesForCategory = workoutRepository.getExercisesByCategoryId(entity.categoryId!!)

            if(exercisesForCategory.isEmpty())
            {
                workoutRepository.deleteCategory(entity)
                result.value = true
            }
            else
            {
                result.value = false
            }
        }

        return result
    }
    fun getEntities() = workoutRepository.getCategories()
    fun updateCategoriesUserUID(userUID: String) = viewModelScope.launch {
        workoutRepository.updateCategoriesUserUID(userUID)
    }
    suspend fun getEntitiesApi() = workoutRepository.getCategoriesApi()
}