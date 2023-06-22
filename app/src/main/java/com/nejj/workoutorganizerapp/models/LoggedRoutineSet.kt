package com.nejj.workoutorganizerapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nejj.workoutorganizerapp.models.relations.RoutineSetsWithExercise
import java.io.Serializable

@Entity(
    tableName = "logged_routine_sets"
)
data class LoggedRoutineSet(
    @PrimaryKey(autoGenerate = true)
    var loggedRoutineSetId: Long? = null,
    var loggedRoutineId: Long? = 0,
    var exerciseId: Long? = 0,
    var exerciseName: String = "",
    val warmupSetsCount: Int = 0,
    var setsCount: Int = 0,
    var setsOrder: Int = 0,
    var userUID: String? = ""
) : Serializable, ReorderableItem {

    override fun getItemText(): String? {
        return exerciseName
    }

    constructor(routineSetWithExercise: RoutineSetsWithExercise, loggedRoutineId: Long) : this(
        loggedRoutineId = loggedRoutineId,
        exerciseId = routineSetWithExercise.exercise.exerciseId,
        exerciseName = routineSetWithExercise.exercise.name,
        warmupSetsCount = routineSetWithExercise.routineSet.warmupSetsCount,
        setsCount = routineSetWithExercise.routineSet.setsCount,
        setsOrder = routineSetWithExercise.routineSet.setsOrder
    )

    fun getTotalSets(): Int = warmupSetsCount + setsCount
}
