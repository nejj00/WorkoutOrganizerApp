package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nejj.workoutorganizerapp.models.LoggedWorkoutRoutine
import com.nejj.workoutorganizerapp.models.relations.LoggedWorkoutRoutineWithLoggedRoutineSets

@Dao
interface LoggedWorkoutRoutineDao : DataAccessObject<LoggedWorkoutRoutine> {

    @Query("SELECT * FROM logged_workout_routines")
    fun getAllEntities(): LiveData<List<LoggedWorkoutRoutine>>

    @Transaction
    @Query("SELECT * FROM logged_workout_routines")
    fun getLoggedWorkoutRoutineWithLoggedRoutineSets(): LiveData<List<LoggedWorkoutRoutineWithLoggedRoutineSets>>

    @Transaction
    @Query("SELECT * FROM logged_workout_routines WHERE loggedRoutineId = :loggedRoutineId")
    suspend fun getLogWorkoutWithSets(loggedRoutineId: Long): LoggedWorkoutRoutineWithLoggedRoutineSets
}