package com.nejj.workoutorganizerapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
   tableName = "exercise_categories"
)
data class ExerciseCategory(
    @PrimaryKey(autoGenerate = true)
    var categoryId: Long? = null,
    var name: String = "",
    var isUserMade: Boolean = false,
    var userUID: String? = ""
) : Serializable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ExerciseCategory) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String = name
}
