package com.nejj.workoutorganizerapp.models.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.ExerciseCategory

data class CategoryWithExercises (
    @Embedded val category: ExerciseCategory,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"
    )
    val exercises: List<Exercise>
)