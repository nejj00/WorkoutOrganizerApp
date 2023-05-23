package com.nejj.workoutorganizerapp.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.nejj.workoutorganizerapp.models.RoutineSet
import com.nejj.workoutorganizerapp.models.WorkoutRoutine

data class WorkoutRoutineWithRoutineSets(
    @Embedded val workoutRoutine: WorkoutRoutine? = null,
    @Relation(
        parentColumn = "routineId",
        entityColumn = "routineId"
    )
    val workoutRoutineSets: List<RoutineSet>? = null
)
