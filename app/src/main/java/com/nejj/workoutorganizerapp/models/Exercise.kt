package com.nejj.workoutorganizerapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "exercises"
)
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String? = "",
    val category: String? = "",
    val type: String? = "",
    val isSingleSide: Boolean? = false
) : Serializable