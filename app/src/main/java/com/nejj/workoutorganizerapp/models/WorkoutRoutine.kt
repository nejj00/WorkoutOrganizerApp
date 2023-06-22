package com.nejj.workoutorganizerapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "workout_routines"
)
data class WorkoutRoutine(
    @PrimaryKey(autoGenerate = true)
    var routineId: Long? = null,
    var name: String = "",
    var notes: String = "",
    var isUserMade: Boolean = false,
    var userUID: String? = null
): Serializable