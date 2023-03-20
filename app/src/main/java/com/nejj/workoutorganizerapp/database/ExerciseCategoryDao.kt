package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nejj.workoutorganizerapp.models.ExerciseCategory

@Dao
interface ExerciseCategoryDao : DataAccessObject<ExerciseCategory> {

    @Query("SELECT * FROM exercise_categories")
    fun getAllEntities(): LiveData<List<ExerciseCategory>>
}