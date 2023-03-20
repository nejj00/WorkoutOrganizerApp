package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.nejj.workoutorganizerapp.models.Exercise

@Dao
interface ExerciseDao : DataAccessObject<Exercise> {

    @Query("SELECT * FROM exercises")
    fun getAllEntities(): LiveData<List<Exercise>>
}