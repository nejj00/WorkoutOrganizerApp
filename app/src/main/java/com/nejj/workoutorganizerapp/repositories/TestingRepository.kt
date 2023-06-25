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
        categories.add(ExerciseCategory(1, "Abs"))
        categories.add(ExerciseCategory(2,"Chest"))
        categories.add(ExerciseCategory(3,"Biceps"))
        categories.add(ExerciseCategory(4,"Back"))
        categories.add(ExerciseCategory(5,"Triceps"))
        categories.add(ExerciseCategory(6,"Legs"))
        categories.add(ExerciseCategory(7,"HORSE NECK"))

        return categories
    }

    fun getExercises() : MutableList<Exercise> {
        val exercises: MutableList<Exercise> = mutableListOf()
        exercises.add(Exercise(null, 1,"Sit Up",  ExerciseType.STRENGTH_WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, 3,"Curls",  ExerciseType.STRENGTH_WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, 1,"Leg Press",  ExerciseType.STRENGTH_WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, 6,"Squat",  ExerciseType.STRENGTH_WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, 7,"Neck Curls",  ExerciseType.STRENGTH_WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, 2,"Push up",  ExerciseType.BODYWEIGHT_WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, 2,"Bench Press",  ExerciseType.STRENGTH_WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, 4,"Pull up",  ExerciseType.BODYWEIGHT_WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, 4,"Barbell Row",  ExerciseType.STRENGTH_WEIGHT_REPS.toString(), false, false))
        exercises.add(Exercise(null, 5,"Skullcrushers",  ExerciseType.STRENGTH_WEIGHT_REPS.toString(), false, false))

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