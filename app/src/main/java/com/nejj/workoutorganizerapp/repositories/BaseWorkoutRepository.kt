package com.nejj.workoutorganizerapp.repositories

import androidx.lifecycle.LiveData
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.ExerciseCategory

interface BaseWorkoutRepository {

    suspend fun upsertExercise(exercise: Exercise) : Long

    suspend fun deleteExercise(exercise: Exercise)

    fun getAllExercises() : LiveData<List<Exercise>>

    suspend fun upsertCategory(exerciseCategory: ExerciseCategory) : Long

    suspend fun deleteCategory(exerciseCategory: ExerciseCategory)

    fun getAllCategories() : LiveData<List<ExerciseCategory>>
}