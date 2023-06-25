package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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
    workoutRepository: WorkoutRepository
) : MainViewModel<Exercise>(app, workoutRepository, "exercises") {

    fun insertEntity(entity: Exercise) = viewModelScope.launch {
        workoutRepository.upsertExercise(entity)
    }

    fun deleteEntity(entity: Exercise) = viewModelScope.launch {
        workoutRepository.deleteExercise(entity)
    }

    fun getEntities() = workoutRepository.getExercises()

    fun getExercisesByCategoryIdLive(categoryId: Long) = workoutRepository.getExercisesByCategoryIdLive(categoryId)

    fun updateExercisesUserUID(userUID: String) = viewModelScope.launch {
        workoutRepository.updateExercisesUserUID(userUID)
    }

    override val classToken: Class<Exercise>
        get() = Exercise::class.java

    override suspend fun getLocalEntitiesList(): List<Exercise> {
        return workoutRepository.getUserMadeExercisesList()
    }

    override fun getIdFieldName(): String {
        return "exerciseId"
    }

    override fun insertToFirestoreList(
        document: DocumentSnapshot,
        entitiesListFirestore: MutableList<Exercise>
    ) {
        entitiesListFirestore.add(document.toObject(classToken)!!)
    }

    override fun getMapFromEntity(entity: Exercise): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["exerciseId"] = entity.exerciseId!!
        map["name"] = entity.name
        map["categoryId"] = entity.categoryId!!
        map["type"] = entity.type
        map["isSingleSide"] = entity.isSingleSide
        map["isUserMade"] = entity.isUserMade
        map["userUID"] = entity.userUID ?: ""

        return map
    }

    override fun getEntityId(entity: Exercise): Long {
        return entity.exerciseId!!
    }
}