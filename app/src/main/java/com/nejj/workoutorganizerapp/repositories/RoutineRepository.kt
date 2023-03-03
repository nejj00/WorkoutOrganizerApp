package com.nejj.workoutorganizerapp.repositories

import com.nejj.workoutorganizerapp.models.WorkoutRoutine

class RoutineRepository() {

    private var workoutRoutines: MutableList<WorkoutRoutine> = mutableListOf<WorkoutRoutine>()

    init {
        for(i in 1..10) {
            workoutRoutines.add(WorkoutRoutine("routine ${i}", "note", ))
        }
    }

    fun getWorkoutRoutines() : MutableList<WorkoutRoutine> = workoutRoutines

}