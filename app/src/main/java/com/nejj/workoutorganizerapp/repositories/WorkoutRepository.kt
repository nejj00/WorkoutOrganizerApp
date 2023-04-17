package com.nejj.workoutorganizerapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nejj.workoutorganizerapp.api.RetrofitInstance
import com.nejj.workoutorganizerapp.database.WorkoutDatabase
import com.nejj.workoutorganizerapp.models.CategoriesResponse
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.util.Resource
import retrofit2.Response

class WorkoutRepository(
    val database: WorkoutDatabase
) {
    suspend fun upsertCategory(exerciseCategory: ExerciseCategory) = database.getExerciseCategoryDao().upsert(exerciseCategory)

    suspend fun deleteCategory(exerciseCategory: ExerciseCategory) = database.getExerciseCategoryDao().deleteEntity(exerciseCategory)

    fun getCategories() = database.getExerciseCategoryDao().getAllEntities()

    suspend fun getCategoriesApi() = RetrofitInstance.api.getCategories()

    suspend fun upsertExercise(exercise: Exercise) = database.getExerciseDao().upsert(exercise)

    suspend fun deleteExercise(exercise: Exercise) = database.getExerciseDao().deleteEntity(exercise)

    fun getExercises() = database.getExerciseDao().getAllEntities()
}