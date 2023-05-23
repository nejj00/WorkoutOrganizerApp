package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.relations.CategoryWithExercises

@Dao
interface ExerciseDao : DataAccessObject<Exercise> {

    @Query("SELECT * FROM exercises")
    fun getAllEntities(): LiveData<List<Exercise>>

    @Transaction
    @Query("SELECT * FROM exercises WHERE category = :category")
    suspend fun getExercisesOfCategory(category: String): List<CategoryWithExercises>
}