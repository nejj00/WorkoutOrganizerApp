package com.nejj.workoutorganizerapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "logged_workout_routines"
)
data class LoggedWorkoutRoutine(
    @PrimaryKey(autoGenerate = true)
    var loggedRoutineId: Long? = null,
    var name: String = "",
    var bodyweight: Double = 0.0,
    var notes: String = "",
    var date: LocalDate = LocalDate.now(),
    var startTime: LocalTime = LocalTime.now(),
    var endTime: LocalTime? = null,
    var userUID: String? = ""
) : Serializable {

    constructor(routine: WorkoutRoutine) : this(
        name = routine.name,
        notes = routine.notes,
        date = LocalDate.now(),
        startTime = LocalTime.now()
    )
}
