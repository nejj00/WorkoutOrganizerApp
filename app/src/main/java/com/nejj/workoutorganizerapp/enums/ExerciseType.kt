package com.nejj.workoutorganizerapp.enums

enum class ExerciseType(private val typeName: String) {
    WEIGHT_REPS("Weight - Reps"),
    WEIGHT_TIME("Weight - Time"),
    BODYWEIGHT_REPS("Bodyweight - Reps"),
    BODYWEIGHT_TIME("Bodyweight - Time"),
    CARDIO("Cardio"),
    OTHER("Other");

    override fun toString(): String = typeName
}