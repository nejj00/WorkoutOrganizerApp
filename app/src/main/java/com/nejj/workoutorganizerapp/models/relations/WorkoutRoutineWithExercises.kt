package com.nejj.workoutorganizerapp.models.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.RoutineSet
import com.nejj.workoutorganizerapp.models.WorkoutRoutine

data class WorkoutRoutineWithExercises (
    @Embedded val workoutRoutine: WorkoutRoutine,
    @Relation(
        parentColumn = "routineId",
        entityColumn = "exerciseId",
        associateBy = Junction(RoutineSet::class)
    )

    val exercises: List<Exercise>
)
