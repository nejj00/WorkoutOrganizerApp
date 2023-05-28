package com.nejj.workoutorganizerapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nejj.workoutorganizerapp.api.RetrofitInstance
import com.nejj.workoutorganizerapp.database.WorkoutDatabase
import com.nejj.workoutorganizerapp.models.*
import com.nejj.workoutorganizerapp.util.Resource
import retrofit2.Response

class WorkoutRepository(
    val database: WorkoutDatabase
) {
    // Categories
    suspend fun upsertCategory(exerciseCategory: ExerciseCategory) = database.getExerciseCategoryDao().upsert(exerciseCategory)
    suspend fun deleteCategory(exerciseCategory: ExerciseCategory) = database.getExerciseCategoryDao().deleteEntity(exerciseCategory)
    fun getCategories() = database.getExerciseCategoryDao().getAllEntities()

    suspend fun getCategoriesApi() = RetrofitInstance.api.getCategories()

    // Exercises
    suspend fun upsertExercise(exercise: Exercise) = database.getExerciseDao().upsert(exercise)
    suspend fun deleteExercise(exercise: Exercise) = database.getExerciseDao().deleteEntity(exercise)
    fun getExercises() = database.getExerciseDao().getAllEntities()
    suspend fun getExercisesByCategoryId(category: String) = database.getExerciseDao().getExercisesOfCategory(category)

    // Workout Routines
    suspend fun upsertWorkoutRoutine(workoutRoutine: WorkoutRoutine) = database.getWorkoutRoutineDao().upsert(workoutRoutine)
    suspend fun deleteWorkoutRoutine(workoutRoutine: WorkoutRoutine) = database.getWorkoutRoutineDao().deleteEntity(workoutRoutine)
    fun getWorkoutRoutines() = database.getWorkoutRoutineDao().getAllEntities()
    fun getWorkoutRoutineWithRoutineSets(routineId: Long) = database.getWorkoutRoutineDao().getWorkoutRoutineWithRoutineSets(routineId)
    fun getWorkoutRoutineWithExercises(routineId: Long) = database.getWorkoutRoutineDao().getWorkoutRoutineWithExercises(routineId)

    // Routine Sets
    suspend fun upsertRoutineSet(routineSet: RoutineSet) = database.getRoutineSetDao().upsert(routineSet)
    suspend fun deleteRoutineSet(routineSet: RoutineSet) = database.getRoutineSetDao().deleteEntity(routineSet)
    fun getRoutineSets() = database.getRoutineSetDao().getAllEntities()
    fun getRoutineSetsWithExercise(routineId: Long) = database.getRoutineSetDao().getRoutineSetsWithExercise(routineId)
    suspend fun getRoutineSetMaxOrder(routineId: Long) = database.getRoutineSetDao().getMaxOrder(routineId)
    fun getRoutineSetsByRoutineId(routineId: Long) = database.getRoutineSetDao().getRoutineSetsByRoutineId(routineId)

    // Logged Workout Routines
    suspend fun upsertLoggedWorkoutRoutine(loggedWorkoutRoutine: LoggedWorkoutRoutine) = database.getLoggedWorkoutRoutineDao().upsert(loggedWorkoutRoutine)
    suspend fun deleteLoggedWorkoutRoutine(loggedWorkoutRoutine: LoggedWorkoutRoutine) = database.getLoggedWorkoutRoutineDao().deleteEntity(loggedWorkoutRoutine)
    fun getLoggedWorkoutRoutines() = database.getLoggedWorkoutRoutineDao().getAllEntities()
    fun getLoggedWorkoutRoutineWithLoggedRoutineSets() = database.getLoggedWorkoutRoutineDao().getLoggedWorkoutRoutineWithLoggedRoutineSets()
    suspend fun getLogWorkoutWithSets(loggedWorkoutId: Long) = database.getLoggedWorkoutRoutineDao().getLogWorkoutWithSets(loggedWorkoutId)

    // Logged Routine Sets
    suspend fun upsertLoggedRoutineSet(loggedRoutineSet: LoggedRoutineSet) = database.getLoggedRoutineSetDao().upsert(loggedRoutineSet)
    suspend fun deleteLoggedRoutineSet(loggedRoutineSet: LoggedRoutineSet) = database.getLoggedRoutineSetDao().deleteEntity(loggedRoutineSet)
    fun getLoggedRoutineSets() = database.getLoggedRoutineSetDao().getAllEntities()
    fun getLoggedRoutineSetsWithLoggedExerciseSets(loggedRoutineId: Long) = database.getLoggedRoutineSetDao().getLoggedRoutineSetsWithLoggedExerciseSets(loggedRoutineId)
    suspend fun getLoggedRouitneSetMaxOrder(loggedRoutineId: Long) = database.getLoggedRoutineSetDao().getMaxOrder(loggedRoutineId)

    // Logged Exercise Sets
    suspend fun upsertLoggedExerciseSet(loggedExerciseSet: LoggedExerciseSet) = database.getLoggedExerciseSetDao().upsert(loggedExerciseSet)
    suspend fun deleteLoggedExerciseSet(loggedExerciseSet: LoggedExerciseSet) = database.getLoggedExerciseSetDao().deleteEntity(loggedExerciseSet)
    fun getLoggedExerciseSets() = database.getLoggedExerciseSetDao().getAllEntities()
    suspend fun getLoggedExerciseSetMaxOrder(loggedRoutineSetId: Long) = database.getLoggedExerciseSetDao().getMaxOrder(loggedRoutineSetId)
}