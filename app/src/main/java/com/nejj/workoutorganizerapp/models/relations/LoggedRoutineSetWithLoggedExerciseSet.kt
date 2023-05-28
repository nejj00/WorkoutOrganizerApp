package com.nejj.workoutorganizerapp.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet

data class LoggedRoutineSetWithLoggedExerciseSet(
    @Embedded val loggedRoutineSet: LoggedRoutineSet,
    @Relation(
        parentColumn = "loggedRoutineSetId",
        entityColumn = "loggedRoutineSetId"
    )

    val loggedExerciseSets: List<LoggedExerciseSet>
) : java.io.Serializable
