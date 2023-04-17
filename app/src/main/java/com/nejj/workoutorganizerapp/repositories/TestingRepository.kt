package com.nejj.workoutorganizerapp.repositories

import com.nejj.workoutorganizerapp.models.*

class TestingRepository() {


    fun getWorkoutRoutines() : MutableList<WorkoutRoutine> {
        val workoutRoutines: MutableList<WorkoutRoutine> = mutableListOf()
        for(i in 1..10) {
            workoutRoutines.add(WorkoutRoutine(name = "routine ${i}", notes = "note", workoutExercises = getWorkoutExercises()))
        }

        return workoutRoutines
    }

    fun getCategories() : MutableList<ExerciseCategory> {
        val categories: MutableList<ExerciseCategory> = mutableListOf()
        categories.add(ExerciseCategory(null, "Abs"))
        categories.add(ExerciseCategory(null,"Chest"))
        categories.add(ExerciseCategory(null,"Biceps"))
        categories.add(ExerciseCategory(null,"Back"))
        categories.add(ExerciseCategory(null,"Triceps"))
        categories.add(ExerciseCategory(null,"Legs"))
        categories.add(ExerciseCategory(null,"HORSE NECK"))

        return categories
    }

    fun getExercises() : MutableList<Exercise> {
        val exercises: MutableList<Exercise> = mutableListOf()
        exercises.add(Exercise(null, "Crunches", "Abs", "Strength Weight", false))
        exercises.add(Exercise(null, "Curls", "Abs", "Strength Weight", false))
        exercises.add(Exercise(null, "Leg Press", "Abs", "Strength Weight", false))
        exercises.add(Exercise(null, "Squat", "Abs", "Strength Weight", false))
        exercises.add(Exercise(null, "Neck Curls", "Abs", "Strength Weight", false))
        exercises.add(Exercise(null, "Push up", "Abs", "Strength Weight", false))

        return exercises
    }

    fun getWorkoutExercises() : MutableList<WorkoutExercise> {
        val workoutExercises: MutableList<WorkoutExercise> = mutableListOf()
        for(exercise in getExercises()) {
            val exerciseSets: MutableList<ExerciseSet> = mutableListOf()
            exerciseSets.add(ExerciseSet())
            exerciseSets.add(ExerciseSet())
            exerciseSets.add(ExerciseSet())
            workoutExercises.add(WorkoutExercise(exercise, exerciseSets))
        }

        return workoutExercises
    }
}