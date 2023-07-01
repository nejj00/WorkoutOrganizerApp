package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.MapInfo
import androidx.room.Query
import androidx.room.Transaction
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.models.RoutineSet
import com.nejj.workoutorganizerapp.models.relations.LoggedRoutineSetWithLoggedExerciseSet
import java.time.LocalDate

@Dao
interface LoggedRoutineSetDao : DataAccessObject<LoggedRoutineSet> {

    @Query("SELECT * FROM logged_routine_sets WHERE loggedRoutineSetId = :id")
    suspend fun getEntityById(id: Long): LoggedRoutineSet

    @Query("SELECT * FROM logged_routine_sets")
    fun getAllEntities(): LiveData<List<LoggedRoutineSet>>

    @Query("SELECT * FROM logged_routine_sets")
    suspend fun getAllEntitiesList(): List<LoggedRoutineSet>

    @Transaction
    @Query("SELECT * FROM logged_routine_sets WHERE loggedRoutineId = :loggedRoutineId")
    fun getLoggedRoutineSetsWithLoggedExerciseSets(loggedRoutineId: Long): LiveData<List<LoggedRoutineSetWithLoggedExerciseSet>>

    @Query("SELECT MAX(setsOrder) FROM logged_routine_sets WHERE loggedRoutineId = :loggedRoutineId")
    suspend fun getMaxOrder(loggedRoutineId: Long): Int?

    @Query("SELECT * FROM logged_routine_sets WHERE loggedRoutineId = :loggedRoutineId")
    suspend fun getLoggedRoutineSetsByRoutineId(loggedRoutineId: Long): List<LoggedRoutineSet>

    @MapInfo(keyColumn = "date", valueColumn = "maxSets")
    @Query("SELECT MAX(setsCount) AS maxSets, logged_workout_routines.date AS date FROM logged_routine_sets " +
            "INNER JOIN logged_workout_routines ON logged_routine_sets.loggedRoutineId = logged_workout_routines.loggedRoutineId " +
            "WHERE exerciseId = :exerciseId")
    suspend fun getMaxSetsCountForExercise(exerciseId: Long): Map<LocalDate?, Int?>

    @Transaction
    @Query("UPDATE logged_routine_sets SET userUID = :userUID")
    suspend fun updateLoggedRoutineSetsUserUID(userUID: String)

    @Query("DELETE FROM logged_routine_sets")
    suspend fun deleteAllLoggedRoutineSets()

}