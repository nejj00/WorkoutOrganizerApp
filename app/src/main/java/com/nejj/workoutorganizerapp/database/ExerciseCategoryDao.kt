package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nejj.workoutorganizerapp.models.ExerciseCategory

@Dao
interface ExerciseCategoryDao : DataAccessObject<ExerciseCategory> {

    @Query("SELECT * FROM exercise_categories")
    fun getAllEntities(): LiveData<List<ExerciseCategory>>

    @Query("SELECT * FROM exercise_categories")
    suspend fun getAllEntitiesList(): List<ExerciseCategory>

    @Query("SELECT * FROM exercise_categories WHERE isUserMade = 1")
    suspend fun getAllUserMadeEntitiesList(): List<ExerciseCategory>

    @Transaction
    @Query("UPDATE exercise_categories SET userUID = :userUID WHERE isUserMade = 1")
    suspend fun updateCategoriesUserUID(userUID: String)

    @Query("DELETE FROM exercise_categories WHERE isUserMade = 1")
    suspend fun deleteAllCategories()
}