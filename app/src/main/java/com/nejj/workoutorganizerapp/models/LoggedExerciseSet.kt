package com.nejj.workoutorganizerapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "logged_exercise_sets"
)
data class LoggedExerciseSet(
    @PrimaryKey(autoGenerate = true)
    val loggedExerciseSetId: Long? = null,
    val loggedRoutineId: Long?,
    val loggedRoutineSetId: Long?,
    val weight: Double = 0.0,
    val reps: Int = 0,
    val setOrder: Int = 0,
    val isWarmupSet: Boolean = false,
    val notes: String = ""
) : Serializable {

    constructor(loggedRoutineSet: LoggedRoutineSet, order: Int, isWarmupSet: Boolean): this(
        loggedRoutineId = loggedRoutineSet.loggedRoutineId,
        loggedRoutineSetId = loggedRoutineSet.loggedRoutineSetId,
        weight = 0.0,
        reps = 0,
        setOrder = order,
        isWarmupSet = isWarmupSet,
        notes = ""
    )
}

