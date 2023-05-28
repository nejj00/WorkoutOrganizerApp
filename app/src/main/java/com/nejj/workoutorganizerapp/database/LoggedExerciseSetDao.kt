package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet

@Dao
interface LoggedExerciseSetDao : DataAccessObject<LoggedExerciseSet> {

    @Query("SELECT * FROM logged_exercise_sets")
    fun getAllEntities(): LiveData<List<LoggedExerciseSet>>

    @Query("SELECT MAX(setOrder) FROM logged_exercise_sets WHERE loggedRoutineSetId = :loggedRoutineSetId")
    suspend fun getMaxOrder(loggedRoutineSetId: Long): Int?
}