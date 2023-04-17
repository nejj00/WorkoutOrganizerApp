package com.nejj.workoutorganizerapp.models

import java.io.Serializable

data class ExerciseSet(
    val weight: Int = 0,
    val repetitions: Int = 0,
    val notes: String = ""
): Serializable