package com.nejj.workoutorganizerapp.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.RoutineSet

data class RoutineSetsWithExercise(
    @Embedded val routineSet: RoutineSet,
    @Relation(
        parentColumn = "exerciseId",
        entityColumn = "exerciseId"
    )
    val exercise: Exercise
)
