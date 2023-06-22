package com.nejj.workoutorganizerapp.models

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "routine_sets",
    indices = [Index(value = ["exerciseId"])]
)
data class RoutineSet(
    @PrimaryKey(autoGenerate = true)
    val routineSetId: Long? = null,
    val routineId: Long? = null,
    var exerciseId: Long? = null,
    var warmupSetsCount: Int = 0,
    var setsCount: Int = 0,
    var setsOrder: Int = 0,
    var isUserMade: Boolean = false,
    val userUID: String? = "",
) : Serializable, ReorderableItem {
    override fun getItemText(): String? {
        return exerciseId.toString()
    }
}
