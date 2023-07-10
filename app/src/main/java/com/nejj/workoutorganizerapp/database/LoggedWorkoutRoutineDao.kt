package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.MapInfo
import androidx.room.Query
import androidx.room.Transaction
import com.nejj.workoutorganizerapp.models.LoggedWorkoutRoutine
import com.nejj.workoutorganizerapp.models.relations.LoggedWorkoutRoutineWithLoggedRoutineSets
import java.time.LocalDate

@Dao
interface LoggedWorkoutRoutineDao : DataAccessObject<LoggedWorkoutRoutine> {

    @Query("SELECT * FROM logged_workout_routines")
    fun getAllEntities(): LiveData<List<LoggedWorkoutRoutine>>

    @Query("SELECT * FROM logged_workout_routines")
    suspend fun getAllEntitiesList(): List<LoggedWorkoutRoutine>

    @Transaction
    @Query("SELECT * FROM logged_workout_routines")
    fun getLoggedWorkoutRoutineWithLoggedRoutineSetsLive(): LiveData<List<LoggedWorkoutRoutineWithLoggedRoutineSets>>

    @Transaction
    @Query("SELECT * FROM logged_workout_routines")
    suspend fun getLoggedWorkoutRoutineWithLoggedRoutineSets(): List<LoggedWorkoutRoutineWithLoggedRoutineSets>

    @Transaction
    @Query("SELECT * FROM logged_workout_routines WHERE loggedRoutineId = :loggedRoutineId")
    suspend fun getLogWorkoutWithSets(loggedRoutineId: Long): LoggedWorkoutRoutineWithLoggedRoutineSets

    @MapInfo(keyColumn = "date", valueColumn = "bodyweight")
    @Query("SELECT bodyweight, date FROM logged_workout_routines ORDER BY date ASC")
    suspend fun getBodyweightOvertime(): Map<LocalDate, Double>


    @Transaction
    @Query("UPDATE logged_workout_routines SET userUID = :userUID")
    suspend fun updateLoggedWorkoutRoutinesUserUID(userUID: String)

    @Query("DELETE FROM logged_workout_routines")
    suspend fun deleteAllLoggedWorkoutRoutines()

}