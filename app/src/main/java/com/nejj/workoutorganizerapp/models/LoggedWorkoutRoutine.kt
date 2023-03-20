package com.nejj.workoutorganizerapp.models

import java.io.Serializable
import java.util.Date

data class LoggedWorkoutRoutine(
    val workoutRoutine: WorkoutRoutine,
    val date: Date
) : Serializable
