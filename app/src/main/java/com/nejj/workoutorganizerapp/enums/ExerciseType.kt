package com.nejj.workoutorganizerapp.enums

enum class ExerciseType(private val typeName: String) {
    STRENGTH_WEIGHT_REPS("Strength: Weight - Reps"),
    STRENGTH_WEIGHT_TIME("Strength: Weight - Time"),
    BODYWEIGHT_WEIGHT_REPS("Bodyweight: Weight - Reps"),
    BODYWEIGHT_REPS("Bodyweight - Reps"),
    BODYWEIGHT_TIME("Bodyweight - Time"),
    CARDIO("Cardio"),
    OTHER("Other");

    override fun toString(): String = typeName
}