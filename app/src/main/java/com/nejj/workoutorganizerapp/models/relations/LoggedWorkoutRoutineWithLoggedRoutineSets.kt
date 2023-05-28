package com.nejj.workoutorganizerapp.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.models.LoggedWorkoutRoutine
import java.io.Serializable

data class LoggedWorkoutRoutineWithLoggedRoutineSets(
    @Embedded val loggedWorkoutRoutine: LoggedWorkoutRoutine,
    @Relation(
        parentColumn = "loggedRoutineId",
        entityColumn = "loggedRoutineId"
    )

    val loggedRoutineSets: List<LoggedRoutineSet>
) : Serializable
