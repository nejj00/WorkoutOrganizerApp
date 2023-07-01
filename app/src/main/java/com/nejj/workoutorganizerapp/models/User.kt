package com.nejj.workoutorganizerapp.models

import java.time.LocalDateTime

data class User(
    val userUID: String,
    val email: String,
    val bodyweight: Double,
    val userCreated: LocalDateTime,
    val userLastLogin: LocalDateTime,
)
