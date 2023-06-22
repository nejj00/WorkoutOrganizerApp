package com.nejj.workoutorganizerapp.repositories

import com.nejj.workoutorganizerapp.enums.ExerciseType
import com.nejj.workoutorganizerapp.models.*

class TestingRepository() {


    fun getWorkoutRoutines() : MutableList<WorkoutRoutine> {
        val workoutRoutines: MutableList<WorkoutRoutine> = mutableListOf()
        for(i in 1..5) {
            workoutRoutines.add(WorkoutRoutine(name = "routine ${i}", notes = "note"))
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
        exercises.add(Exercise(null, "Sit Up", "Abs", ExerciseType.WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, "Curls", "Biceps", ExerciseType.WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, "Leg Press", "Abs", ExerciseType.WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, "Squat", "Legs", ExerciseType.WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, "Neck Curls", "HORSE NECK", ExerciseType.WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, "Push up", "Chest", ExerciseType.WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, "Bench Press", "Chest", ExerciseType.WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, "Pull up", "Back", ExerciseType.WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, "Barbell Row", "Back", ExerciseType.WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, "Skullcrushers", "Triceps", ExerciseType.WEIGHT_REPS.toString(), false, false))

        return exercises
    }

    fun getRoutineSets() : MutableList<RoutineSet> {
        val routineSets: MutableList<RoutineSet> = mutableListOf()

        for(i in 10..60) {
            if(i % 10 == 0)
                continue
            routineSets.add(RoutineSet(null, (i / 10).toLong(), (i % 10).toLong(), 0, 3, i % 10))
        }

        return routineSets
    }
}