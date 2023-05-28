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
    val loggedRoutineId: Long? = null,
    var name: String = "",
    var bodyweight: Double = 0.0,
    var notes: String = "",
    val date: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.now(),
    val endTime: LocalTime? = null,
) : Serializable {

    constructor(routine: WorkoutRoutine) : this(
        name = routine.name,
        notes = routine.notes,
        date = LocalDate.now(),
        startTime = LocalTime.now()
    )
}
