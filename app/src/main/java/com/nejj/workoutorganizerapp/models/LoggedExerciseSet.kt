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
    var loggedRoutineId: Long? = null,
    var loggedRoutineSetId: Long? = null,
    var weight: Double = 0.0,
    var reps: Int = 0,
    var setOrder: Int = 0,
    val isWarmupSet: Boolean = false,
    var notes: String = "",
    var userUID: String? = ""
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

