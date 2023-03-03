package com.nejj.workoutorganizerapp.models

import java.io.Serializable

data class Exercise(
    val name: String,
    val category: String,
    val type: String,
    val isSingleSide: Boolean

) : Serializable