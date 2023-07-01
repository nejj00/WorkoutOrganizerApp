package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.MapInfo
import androidx.room.Query
import androidx.room.Transaction
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import java.time.LocalDate

@Dao
interface LoggedExerciseSetDao : DataAccessObject<LoggedExerciseSet> {

    @Query("SELECT * FROM logged_exercise_sets")
    fun getAllEntities(): LiveData<List<LoggedExerciseSet>>

    @Query("SELECT * FROM logged_exercise_sets")
    suspend fun getAllEntitiesList(): List<LoggedExerciseSet>

    @Query("SELECT * FROM logged_exercise_sets WHERE loggedRoutineId = :loggedRoutineId")
    suspend fun getExerciseSetsForLoggedRoutine(loggedRoutineId: Long): List<LoggedExerciseSet>

    @Query("SELECT * FROM logged_exercise_sets WHERE loggedRoutineSetId = :loggedRoutineSetId")
    suspend fun getExerciseSetsForLoggedRoutineSet(loggedRoutineSetId: Long): List<LoggedExerciseSet>

    @Query("SELECT MAX(setOrder) FROM logged_exercise_sets WHERE loggedRoutineSetId = :loggedRoutineSetId")
    suspend fun getMaxOrder(loggedRoutineSetId: Long): Int?


    @Transaction
    @MapInfo(valueColumn = "date")
    @Query("SELECT logged_exercise_sets.*, logged_workout_routines.date AS date FROM logged_exercise_sets " +
            "INNER JOIN logged_workout_routines ON logged_exercise_sets.loggedRoutineId = logged_workout_routines.loggedRoutineId " +
            "ORDER BY logged_workout_routines.date ASC")
    suspend fun getLoggedExerciseSetsWithDateMap(): Map<LoggedExerciseSet, LocalDate>

    @Transaction
    @MapInfo(valueColumn = "date")
    @Query("SELECT logged_exercise_sets.*, logged_workout_routines.date AS date FROM logged_exercise_sets " +
            "INNER JOIN logged_routine_sets ON logged_exercise_sets.loggedRoutineSetId = logged_routine_sets.loggedRoutineSetId " +
            "INNER JOIN logged_workout_routines ON logged_routine_sets.loggedRoutineId = logged_workout_routines.loggedRoutineId " +
            "INNER JOIN routine_sets ON logged_routine_sets.exerciseId = routine_sets.exerciseId " +
            "INNER JOIN exercises ON routine_sets.exerciseId = exercises.exerciseId " +
            "INNER JOIN exercise_categories ON exercises.categoryId = exercise_categories.categoryId " +
            "WHERE exercise_categories.categoryId = :categoryId " +
            "ORDER BY logged_workout_routines.date ASC")
    suspend fun getLoggedExerciseSetsWithDateMapByCategory(categoryId: Long): Map<LoggedExerciseSet, LocalDate>

    @Transaction
    @MapInfo(valueColumn = "date")
    @Query("SELECT logged_exercise_sets.*, logged_workout_routines.date AS date FROM logged_exercise_sets " +
            "INNER JOIN logged_routine_sets ON logged_exercise_sets.loggedRoutineSetId = logged_routine_sets.loggedRoutineSetId " +
            "INNER JOIN logged_workout_routines ON logged_routine_sets.loggedRoutineId = logged_workout_routines.loggedRoutineId " +
            "INNER JOIN routine_sets ON logged_routine_sets.exerciseId = routine_sets.exerciseId " +
            "WHERE logged_routine_sets.exerciseId = :exerciseId " +
            "ORDER BY logged_workout_routines.date ASC")
    suspend fun getLoggedExerciseSetsWithDateMapByExercise(exerciseId: Long): Map<LoggedExerciseSet, LocalDate>

    @MapInfo(keyColumn = "date", valueColumn = "maxWeight")
    @Query("SELECT MAX(weight) AS maxWeight, logged_workout_routines.date AS date FROM logged_exercise_sets " +
            "INNER JOIN logged_workout_routines ON logged_exercise_sets.loggedRoutineId = logged_workout_routines.loggedRoutineId " +
            "INNER JOIN logged_routine_sets ON logged_exercise_sets.loggedRoutineSetId = logged_routine_sets.loggedRoutineSetId " +
            "INNER JOIN routine_sets ON logged_routine_sets.exerciseId = routine_sets.exerciseId " +
            "WHERE routine_sets.exerciseId = :exerciseId " +
            "ORDER BY logged_workout_routines.date ASC LIMIT 1")
    suspend fun getMaxWeightForExercise(exerciseId: Long): Map<LocalDate?, Double?>

    @MapInfo(keyColumn = "date", valueColumn = "maxReps")
    @Query("SELECT MAX(reps) AS maxReps, logged_workout_routines.date AS date FROM logged_exercise_sets " +
            "INNER JOIN logged_workout_routines ON logged_exercise_sets.loggedRoutineId = logged_workout_routines.loggedRoutineId " +
            "INNER JOIN logged_routine_sets ON logged_exercise_sets.loggedRoutineSetId = logged_routine_sets.loggedRoutineSetId " +
            "INNER JOIN exercises ON logged_routine_sets.exerciseId = exercises.exerciseId " +
            "WHERE exercises.exerciseId = :exerciseId " +
            "ORDER BY logged_workout_routines.date ASC")
    suspend fun getMaxRepsForExercise(exerciseId: Long): Map<LocalDate?, Int?>

    @Transaction
    @Query("UPDATE logged_exercise_sets SET userUID = :userUID")
    suspend fun updateLoggedExerciseSetsUserUID(userUID: String)

    @Query("DELETE FROM logged_exercise_sets")
    suspend fun deleteAllLoggedExerciseSets()
}