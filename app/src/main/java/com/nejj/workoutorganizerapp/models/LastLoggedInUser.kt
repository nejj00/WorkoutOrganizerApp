package com.nejj.workoutorganizerapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "last_logged_in_user"
)
data class LastLoggedInUser(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,
    var userUID: String? = "",
    val date: LocalDate = LocalDate.now()
)
