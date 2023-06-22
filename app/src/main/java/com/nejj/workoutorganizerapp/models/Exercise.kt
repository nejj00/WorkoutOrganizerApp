package com.nejj.workoutorganizerapp.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "exercises",
    indices = [Index(value = ["name"], unique = true)]
)
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Long? = null,
    val name: String = "",
    val category: String = "",
    val type: String = "",
    val isSingleSide: Boolean = false,
    val isUserMade: Boolean = false,
    val userUID: String? = ""
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Exercise) return false
        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun toString(): String = name
}