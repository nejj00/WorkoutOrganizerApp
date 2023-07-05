package com.nejj.workoutorganizerapp.repositories

import androidx.lifecycle.MutableLiveData
import com.nejj.workoutorganizerapp.models.*

class FakeWorkoutRepository {
    private val categoriesList = mutableListOf<ExerciseCategory>()
    private val exerciseList = mutableListOf<Exercise>()
    private val workoutRoutineList = mutableListOf<WorkoutRoutine>()
    private val routineSetList = mutableListOf<RoutineSet>()
    private val loggedWorkoutRoutineList = mutableListOf<LoggedWorkoutRoutine>()
    private val loggedRoutineSetList = mutableListOf<LoggedRoutineSet>()
    private val loggedExerciseSetSetList = mutableListOf<LoggedExerciseSet>()

    private val observableCategoriesList = MutableLiveData<List<ExerciseCategory>>(categoriesList)
    private val observableExerciseList = MutableLiveData<List<Exercise>>(exerciseList)
    private val observableWorkoutRoutineList = MutableLiveData<List<WorkoutRoutine>>(workoutRoutineList)
    private val observableRoutineSetList = MutableLiveData<List<RoutineSet>>(routineSetList)
    private val observableLoggedWorkoutRoutineList = MutableLiveData<List<LoggedWorkoutRoutine>>(loggedWorkoutRoutineList)
    private val observableLoggedRoutineSetList = MutableLiveData<List<LoggedRoutineSet>>(loggedRoutineSetList)
    private val observableLoggedExerciseSetSetList = MutableLiveData<List<LoggedExerciseSet>>(loggedExerciseSetSetList)

    private fun refreshCategoriesLiveData() {
        observableCategoriesList.postValue(categoriesList)
    }

    private fun refreshExerciseLiveData() {
        observableExerciseList.postValue(exerciseList)
    }

    private fun refreshWorkoutRoutineLiveData() {
        observableWorkoutRoutineList.postValue(workoutRoutineList)
    }

    private fun refreshRoutineSetLiveData() {
        observableRoutineSetList.postValue(routineSetList)
    }

    private fun refreshLoggedWorkoutRoutineLiveData() {
        observableLoggedWorkoutRoutineList.postValue(loggedWorkoutRoutineList)
    }

    private fun refreshLoggedRoutineSetLiveData() {
        observableLoggedRoutineSetList.postValue(loggedRoutineSetList)
    }

    private fun refreshLoggedExerciseSetLiveData() {
        observableLoggedExerciseSetSetList.postValue(loggedExerciseSetSetList)
    }

    // ExerciseCategory methods

    fun upsertCategory(category: ExerciseCategory) {
        val index = categoriesList.indexOfFirst { it.categoryId == category.categoryId }
        if (index != -1) {
            categoriesList[index] = category
        } else {
            categoriesList.add(category)
        }
        refreshCategoriesLiveData()
    }

    fun deleteCategory(category: ExerciseCategory) {
        categoriesList.removeAll { it.categoryId == category.categoryId }
        refreshCategoriesLiveData()
    }

    fun observeCategories() = observableCategoriesList

    // Exercise methods

    fun upsertExercise(exercise: Exercise) {
        val index = exerciseList.indexOfFirst { it.exerciseId == exercise.exerciseId }
        if (index != -1) {
            exerciseList[index] = exercise
        } else {
            exerciseList.add(exercise)
        }
        refreshExerciseLiveData()
    }

    fun deleteExercise(exercise: Exercise) {
        exerciseList.removeAll { it.exerciseId == exercise.exerciseId }
        refreshExerciseLiveData()
    }

    fun observeExercises() = observableExerciseList

    // WorkoutRoutine methods

    fun upsertWorkoutRoutine(workoutRoutine: WorkoutRoutine) {
        val index = workoutRoutineList.indexOfFirst { it.routineId == workoutRoutine.routineId }
        if (index != -1) {
            workoutRoutineList[index] = workoutRoutine
        } else {
            workoutRoutineList.add(workoutRoutine)
        }
        refreshWorkoutRoutineLiveData()
    }

    fun deleteWorkoutRoutine(workoutRoutine: WorkoutRoutine) {
        workoutRoutineList.removeAll { it.routineId == workoutRoutine.routineId }
        refreshWorkoutRoutineLiveData()
    }

    fun observeWorkoutRoutines() = observableWorkoutRoutineList

    // RoutineSet methods

    fun upsertRoutineSet(routineSet: RoutineSet) {
        val index = routineSetList.indexOfFirst { it.routineSetId == routineSet.routineSetId }
        if (index != -1) {
            routineSetList[index] = routineSet
        } else {
            routineSetList.add(routineSet)
        }
        refreshRoutineSetLiveData()
    }

    fun deleteRoutineSet(routineSet: RoutineSet) {
        routineSetList.removeAll { it.routineSetId == routineSet.routineSetId }
        refreshRoutineSetLiveData()
    }

    fun observeRoutineSets() = observableRoutineSetList

    // LoggedWorkoutRoutine methods

    fun upsertLoggedWorkoutRoutine(loggedWorkoutRoutine: LoggedWorkoutRoutine) {
        val index = loggedWorkoutRoutineList.indexOfFirst { it.loggedRoutineId == loggedWorkoutRoutine.loggedRoutineId }
        if (index != -1) {
            loggedWorkoutRoutineList[index] = loggedWorkoutRoutine
        } else {
            loggedWorkoutRoutineList.add(loggedWorkoutRoutine)
        }
        refreshLoggedWorkoutRoutineLiveData()
    }

    fun deleteLoggedWorkoutRoutine(loggedWorkoutRoutine: LoggedWorkoutRoutine) {
        loggedWorkoutRoutineList.removeAll { it.loggedRoutineId == loggedWorkoutRoutine.loggedRoutineId }
        refreshLoggedWorkoutRoutineLiveData()
    }

    fun observeLoggedWorkoutRoutines() = observableLoggedWorkoutRoutineList

    // LoggedRoutineSet methods

    fun upsertLoggedRoutineSet(loggedRoutineSet: LoggedRoutineSet) {
        val index = loggedRoutineSetList.indexOfFirst { it.loggedRoutineSetId == loggedRoutineSet.loggedRoutineId }
        if (index != -1) {
            loggedRoutineSetList[index] = loggedRoutineSet
        } else {
            loggedRoutineSetList.add(loggedRoutineSet)
        }
        refreshLoggedRoutineSetLiveData()
    }

    fun deleteLoggedRoutineSet(loggedRoutineSet: LoggedRoutineSet) {
        loggedRoutineSetList.removeAll { it.loggedRoutineId == loggedRoutineSet.loggedRoutineId }
        refreshLoggedRoutineSetLiveData()
    }

    fun observeLoggedRoutineSets() = observableLoggedRoutineSetList

    // LoggedExerciseSet methods

    fun upsertLoggedExerciseSet(loggedExerciseSet: LoggedExerciseSet) {
        val index = loggedExerciseSetSetList.indexOfFirst { it.loggedExerciseSetId == loggedExerciseSet.loggedExerciseSetId }
        if (index != -1) {
            loggedExerciseSetSetList[index] = loggedExerciseSet
        } else {
            loggedExerciseSetSetList.add(loggedExerciseSet)
        }
        refreshLoggedExerciseSetLiveData()
    }

    fun deleteLoggedExerciseSet(loggedExerciseSet: LoggedExerciseSet) {
        loggedExerciseSetSetList.removeAll { it.loggedExerciseSetId == loggedExerciseSet.loggedExerciseSetId }
        refreshLoggedExerciseSetLiveData()
    }

    fun observeLoggedExerciseSets() = observableLoggedExerciseSetSetList
}
