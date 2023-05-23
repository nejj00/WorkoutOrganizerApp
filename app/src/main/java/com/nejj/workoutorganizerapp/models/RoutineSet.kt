package com.nejj.workoutorganizerapp.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import java.io.Serializable

@Entity(
    tableName = "routine_sets",
    primaryKeys = ["routineId", "exerciseId"],
    indices = [Index(value = ["exerciseId"])]
)
data class RoutineSet(
    val routineId: Long,
    val exerciseId: Long,
    val warmupSetsCount: Int = 0,
    val setsCount: Int = 0,
    val setsOrder: Int = 0
) : Serializable
