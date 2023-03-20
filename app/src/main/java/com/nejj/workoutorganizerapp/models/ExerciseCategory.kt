package com.nejj.workoutorganizerapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
   tableName = "exercise_categories"
)
data class ExerciseCategory(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var categoryName: String
) : Serializable
