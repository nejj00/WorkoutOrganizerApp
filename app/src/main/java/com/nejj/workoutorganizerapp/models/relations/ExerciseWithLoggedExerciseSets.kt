package com.nejj.workoutorganizerapp.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet

//data class ExerciseWithLoggedExerciseSets (
//    @Embedded val exercise: Exercise,
//    @Relation(
//        parentColumn = "exerciseId",
//        entityColumn = "exerciseId"
//    )
//    val exerciseSets: List<LoggedExerciseSet>
//)
