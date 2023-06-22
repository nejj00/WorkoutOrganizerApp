package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.models.relations.CategoryWithExercises

@Dao
interface ExerciseDao : DataAccessObject<Exercise> {

    @Query("SELECT * FROM exercises")
    fun getAllEntities(): LiveData<List<Exercise>>

    @Query("SELECT * FROM exercises")
    suspend fun getAllEntitiesList(): List<Exercise>

    @Query("SELECT * FROM exercises WHERE isUserMade = 1")
    suspend fun getAllUserMadeEntitiesList(): List<Exercise>

    @Transaction
    @Query("SELECT * FROM exercises WHERE category = :category")
    suspend fun getExercisesOfCategory(category: String): List<CategoryWithExercises>

    @Query("SELECT * FROM exercises WHERE category = :category")
    fun getExercisesOfCategoryLive(category: String): LiveData<List<Exercise>>

    @Transaction
    @Query("UPDATE exercises SET userUID = :userUID WHERE isUserMade = 1")
    suspend fun updateExercisesUserUID(userUID: String)

    @Query("DELETE FROM exercises")
    suspend fun deleteAllExercises()

}