package com.nejj.workoutorganizerapp.models

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime

data class WorkoutRoutine(
    val name: String = "",
    val notes: String = "",
    val date: LocalDate = LocalDate.now(),
    val startTime: LocalTime = LocalTime.now(),
    val endTime: LocalTime? = null,
    var workoutExercises: MutableList<WorkoutExercise>? = null
): Serializable