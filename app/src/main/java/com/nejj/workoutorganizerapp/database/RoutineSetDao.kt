package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nejj.workoutorganizerapp.models.RoutineSet
import com.nejj.workoutorganizerapp.models.relations.RoutineSetsWithExercise
import com.nejj.workoutorganizerapp.models.relations.WorkoutRoutineWithRoutineSets

@Dao
interface RoutineSetDao : DataAccessObject<RoutineSet> {

    @Query("SELECT * FROM routine_sets WHERE routineSetId = :routineSetId")
    suspend fun getEntityById(routineSetId: Long): RoutineSet

    @Query("SELECT * FROM routine_sets")
    fun getAllEntities(): LiveData<RoutineSet>

    @Query("SELECT * FROM routine_sets")
    suspend fun getAllEntitiesList(): List<RoutineSet>

    @Query("SELECT * FROM routine_sets WHERE isUserMade = 1")
    suspend fun getAllUserMadeEntitiesList(): List<RoutineSet>

    @Transaction
    @Query("SELECT * FROM routine_sets WHERE routineId = :routineId")
    suspend fun getRoutineSetsWithExercise(routineId: Long): List<RoutineSetsWithExercise>

    @Transaction
    @Query("SELECT * FROM routine_sets WHERE routineId = :routineId")
    fun getRoutineSetsWithExerciseLive(routineId: Long): LiveData<List<RoutineSetsWithExercise>>

    @Query("SELECT MAX(setsOrder) FROM routine_sets WHERE routineId = :routineId")
    suspend fun getMaxOrder(routineId: Long): Int?

    @Query("SELECT * FROM routine_sets WHERE routineId = :routineId")
    suspend fun getRoutineSetsByRoutineId(routineId: Long): List<RoutineSet>

    @Query("SELECT * FROM routine_sets WHERE routineId = :routineId")
    fun getRoutineSetsListByRoutineId(routineId: Long): List<RoutineSet>

    @Query("SELECT * FROM routine_sets WHERE exerciseId = :exerciseId")
    suspend fun getRoutineSetsByExerciseId(exerciseId: Long): List<RoutineSet>

    @Transaction
    @Query("UPDATE routine_sets SET userUID = :userUID WHERE isUserMade = 1")
    suspend fun updateRoutineSetsUserUID(userUID: String)

    @Query("DELETE FROM routine_sets WHERE isUserMade = 1")
    suspend fun deleteAllRoutineSets()

    @Query("DELETE FROM routine_sets WHERE exerciseId = :exerciseId")
    suspend fun deleteRoutineSetsByExerciseId(exerciseId: Long)
}