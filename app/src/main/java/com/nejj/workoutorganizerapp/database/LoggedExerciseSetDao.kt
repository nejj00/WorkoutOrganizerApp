package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet

@Dao
interface LoggedExerciseSetDao : DataAccessObject<LoggedExerciseSet> {

    @Query("SELECT * FROM logged_exercise_sets")
    fun getAllEntities(): LiveData<List<LoggedExerciseSet>>

    @Query("SELECT * FROM logged_exercise_sets")
    suspend fun getAllEntitiesList(): List<LoggedExerciseSet>

    @Query("SELECT * FROM logged_exercise_sets WHERE loggedRoutineId = :loggedRoutineId")
    suspend fun getExerciseSetsForLoggedRoutine(loggedRoutineId: Long): List<LoggedExerciseSet>

    @Query("SELECT MAX(setOrder) FROM logged_exercise_sets WHERE loggedRoutineSetId = :loggedRoutineSetId")
    suspend fun getMaxOrder(loggedRoutineSetId: Long): Int?

    @Transaction
    @Query("UPDATE logged_exercise_sets SET userUID = :userUID")
    suspend fun updateLoggedExerciseSetsUserUID(userUID: String)

    @Query("DELETE FROM logged_exercise_sets")
    suspend fun deleteAllLoggedExerciseSets()
}