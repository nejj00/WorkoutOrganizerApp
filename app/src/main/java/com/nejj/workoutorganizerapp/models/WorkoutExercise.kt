package com.nejj.workoutorganizerapp.models

import java.io.Serializable

data class WorkoutExercise(
    val exercise: Exercise,
    var exerciseSets: MutableList<ExerciseSet>,
): Serializable