package com.nejj.workoutorganizerapp.repositories

import com.nejj.workoutorganizerapp.database.WorkoutDatabase
import com.nejj.workoutorganizerapp.models.ExerciseCategory

class WorkoutRepository(
    val database: WorkoutDatabase
) {
    suspend fun upsert(exerciseCategory: ExerciseCategory) = database.getExerciseCategoryDao().upsert(exerciseCategory)

    suspend fun deleteCategory(exerciseCategory: ExerciseCategory) = database.getExerciseCategoryDao().deleteEntity(exerciseCategory)

    fun getCategories() = database.getExerciseCategoryDao().getAllEntities()

}