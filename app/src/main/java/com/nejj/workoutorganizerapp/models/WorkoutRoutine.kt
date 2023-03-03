package com.nejj.workoutorganizerapp.models

import java.io.Serializable

data class WorkoutRoutine(
    val name: String,
    val notes: String?,
    var workoutExercises: MutableList<WorkoutExercise>? = null
): Serializable