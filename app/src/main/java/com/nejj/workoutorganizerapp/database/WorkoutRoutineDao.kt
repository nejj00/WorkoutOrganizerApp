package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nejj.workoutorganizerapp.models.WorkoutRoutine
import com.nejj.workoutorganizerapp.models.relations.WorkoutRoutineWithExercises
import com.nejj.workoutorganizerapp.models.relations.WorkoutRoutineWithRoutineSets

@Dao
interface WorkoutRoutineDao : DataAccessObject<WorkoutRoutine> {

    @Query("SELECT * FROM workout_routines")
    fun getAllEntities(): LiveData<List<WorkoutRoutine>>

    @Query("SELECT * FROM workout_routines")
    suspend fun getAllEntitiesList(): List<WorkoutRoutine>

    @Query("SELECT * FROM workout_routines WHERE isUserMade = 1")
    suspend fun getAllUserMadeEntitiesList(): List<WorkoutRoutine>

    @Transaction
    @Query("SELECT * FROM workout_routines WHERE routineId = :routineId")
    fun getWorkoutRoutineWithRoutineSets(routineId: Long): LiveData<WorkoutRoutineWithRoutineSets>

    @Transaction
    @Query("SELECT * FROM workout_routines WHERE routineId = :routineId")
    fun getWorkoutRoutineWithExercises(routineId: Long): LiveData<WorkoutRoutineWithExercises>

    @Transaction
    @Query("UPDATE workout_routines SET userUID = :userUID WHERE isUserMade = 1")
    suspend fun updateWorkoutRoutinesUserUID(userUID: String)

    @Query("DELETE FROM workout_routines WHERE isUserMade = 1")
    suspend fun deleteAllWorkoutRoutines()

}