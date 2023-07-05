package com.nejj.workoutorganizerapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDateTime

@Entity(
    tableName = "users"
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Long? = null,
    val userUID: String,
    val email: String,
    val bodyweight: Double,
    val userCreated: LocalDateTime,
    var userLastLogin: LocalDateTime,
) : Serializable
