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

    @Query("SELECT * FROM routine_sets")
    fun getAllEntities(): LiveData<RoutineSet>

//    @Transaction
//    @Query("SELECT * FROM routine_sets WHERE routine_id = :routineId")
//    suspend fun getSetsOfRoutine(routineId: Long): List<WorkoutRoutineWithRoutineSets>

    @Transaction
    @Query("SELECT * FROM routine_sets WHERE routineId = :routineId")
    fun getRoutineSetsWithExercise(routineId: Long): LiveData<List<RoutineSetsWithExercise>>

    @Query("SELECT MAX(setsOrder) FROM routine_sets WHERE routineId = :routineId")
    suspend fun getMaxOrder(routineId: Long): Int

    @Query("SELECT * FROM routine_sets WHERE routineId = :routineId")
    fun getRoutineSetsByRoutineId(routineId: Long): LiveData<List<RoutineSet>>
}