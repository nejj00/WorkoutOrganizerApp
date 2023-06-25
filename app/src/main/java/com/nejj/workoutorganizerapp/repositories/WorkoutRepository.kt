package com.nejj.workoutorganizerapp.repositories

import com.nejj.workoutorganizerapp.api.RetrofitInstance
import com.nejj.workoutorganizerapp.database.WorkoutDatabase
import com.nejj.workoutorganizerapp.models.*

class WorkoutRepository(
    val database: WorkoutDatabase
) {
    // Categories
    suspend fun upsertCategory(exerciseCategory: ExerciseCategory) = database.getExerciseCategoryDao().upsert(exerciseCategory)
    suspend fun deleteCategory(exerciseCategory: ExerciseCategory) = database.getExerciseCategoryDao().deleteEntity(exerciseCategory)
    suspend fun deleteAllCategories() = database.getExerciseCategoryDao().deleteAllCategories()
    fun getCategories() = database.getExerciseCategoryDao().getAllEntities()
    suspend fun getCategoriesList() = database.getExerciseCategoryDao().getAllEntitiesList()
    suspend fun getUserMadeCategoriesList() = database.getExerciseCategoryDao().getAllUserMadeEntitiesList()
    suspend fun updateCategoriesUserUID(userUID: String) = database.getExerciseCategoryDao().updateCategoriesUserUID(userUID)

    suspend fun getCategoriesApi() = RetrofitInstance.api.getCategories()

    // Exercises
    suspend fun upsertExercise(exercise: Exercise) = database.getExerciseDao().upsert(exercise)
    suspend fun deleteExercise(exercise: Exercise) = database.getExerciseDao().deleteEntity(exercise)
    suspend fun deleteAllExercises() = database.getExerciseDao().deleteAllExercises()
    fun getExercises() = database.getExerciseDao().getAllEntities()
    suspend fun getExercisesList() = database.getExerciseDao().getAllEntitiesList()
    suspend fun getUserMadeExercisesList() = database.getExerciseDao().getAllUserMadeEntitiesList()
    suspend fun getExercisesByCategoryId(categoryId: Long) = database.getExerciseDao().getExercisesOfCategory(categoryId)
    fun getExercisesByCategoryIdLive(categoryId: Long) = database.getExerciseDao().getExercisesOfCategoryLive(categoryId)
    suspend fun updateExercisesUserUID(userUID: String) = database.getExerciseDao().updateExercisesUserUID(userUID)

    // Workout Routines
    suspend fun upsertWorkoutRoutine(workoutRoutine: WorkoutRoutine) = database.getWorkoutRoutineDao().upsert(workoutRoutine)
    suspend fun deleteWorkoutRoutine(workoutRoutine: WorkoutRoutine) = database.getWorkoutRoutineDao().deleteEntity(workoutRoutine)
    suspend fun deleteAllWorkoutRoutines() = database.getWorkoutRoutineDao().deleteAllWorkoutRoutines()
    fun getWorkoutRoutines() = database.getWorkoutRoutineDao().getAllEntities()
    suspend fun getWorkoutRoutinesList() = database.getWorkoutRoutineDao().getAllEntitiesList()
    suspend fun getUserMadeWorkoutRoutinesList() = database.getWorkoutRoutineDao().getAllUserMadeEntitiesList()
    fun getWorkoutRoutineWithRoutineSets(routineId: Long) = database.getWorkoutRoutineDao().getWorkoutRoutineWithRoutineSets(routineId)
    fun getWorkoutRoutineWithExercises(routineId: Long) = database.getWorkoutRoutineDao().getWorkoutRoutineWithExercises(routineId)
    suspend fun updateWorkoutRoutinesUserUID(userUID: String) = database.getWorkoutRoutineDao().updateWorkoutRoutinesUserUID(userUID)

    // Routine Sets
    suspend fun upsertRoutineSet(routineSet: RoutineSet) = database.getRoutineSetDao().upsert(routineSet)
    suspend fun deleteRoutineSet(routineSet: RoutineSet) = database.getRoutineSetDao().deleteEntity(routineSet)
    suspend fun deleteAllRoutineSets() = database.getRoutineSetDao().deleteAllRoutineSets()
    suspend fun getRoutineSetById(routineSetId: Long) = database.getRoutineSetDao().getEntityById(routineSetId)
    fun getRoutineSets() = database.getRoutineSetDao().getAllEntities()
    suspend fun getRoutineSetsList() = database.getRoutineSetDao().getAllEntitiesList()
    suspend fun getUserMadeRoutineSetsList() = database.getRoutineSetDao().getAllEntitiesList()
    suspend fun getRoutineSetsWithExercise(routineId: Long) = database.getRoutineSetDao().getRoutineSetsWithExercise(routineId)
    fun getRoutineSetsWithExerciseLive(routineId: Long) = database.getRoutineSetDao().getRoutineSetsWithExerciseLive(routineId)
    suspend fun getRoutineSetMaxOrder(routineId: Long) = database.getRoutineSetDao().getMaxOrder(routineId)
    suspend fun getRoutineSetsByRoutineId(routineId: Long) = database.getRoutineSetDao().getRoutineSetsByRoutineId(routineId)
    fun getRoutineSetsListByRoutineId(routineId: Long) = database.getRoutineSetDao().getRoutineSetsListByRoutineId(routineId)
    suspend fun updateRoutineSetsUserUID(userUID: String) = database.getRoutineSetDao().updateRoutineSetsUserUID(userUID)

    // Logged Workout Routines
    suspend fun upsertLoggedWorkoutRoutine(loggedWorkoutRoutine: LoggedWorkoutRoutine) = database.getLoggedWorkoutRoutineDao().upsert(loggedWorkoutRoutine)
    suspend fun deleteLoggedWorkoutRoutine(loggedWorkoutRoutine: LoggedWorkoutRoutine) = database.getLoggedWorkoutRoutineDao().deleteEntity(loggedWorkoutRoutine)
    suspend fun deleteAllLoggedWorkoutRoutines() = database.getLoggedWorkoutRoutineDao().deleteAllLoggedWorkoutRoutines()
    fun getLoggedWorkoutRoutines() = database.getLoggedWorkoutRoutineDao().getAllEntities()
    suspend fun getLoggedWorkoutRoutinesList() = database.getLoggedWorkoutRoutineDao().getAllEntitiesList()
    fun getLoggedWorkoutRoutineWithLoggedRoutineSetsLive() = database.getLoggedWorkoutRoutineDao().getLoggedWorkoutRoutineWithLoggedRoutineSetsLive()
    suspend fun getLoggedWorkoutRoutineWithLoggedRoutineSets() = database.getLoggedWorkoutRoutineDao().getLoggedWorkoutRoutineWithLoggedRoutineSets()
    suspend fun getLogWorkoutWithSets(loggedWorkoutId: Long) = database.getLoggedWorkoutRoutineDao().getLogWorkoutWithSets(loggedWorkoutId)
    suspend fun updateLoggedWorkoutRoutinesUserUID(userUID: String) = database.getLoggedWorkoutRoutineDao().updateLoggedWorkoutRoutinesUserUID(userUID)

    // Logged Routine Sets
    suspend fun upsertLoggedRoutineSet(loggedRoutineSet: LoggedRoutineSet) = database.getLoggedRoutineSetDao().upsert(loggedRoutineSet)
    suspend fun deleteLoggedRoutineSet(loggedRoutineSet: LoggedRoutineSet) = database.getLoggedRoutineSetDao().deleteEntity(loggedRoutineSet)
    suspend fun deleteAllLoggedRoutineSets() = database.getLoggedRoutineSetDao().deleteAllLoggedRoutineSets()
    suspend fun getLoggedRoutineSetById(loggedRoutineSetId: Long) = database.getLoggedRoutineSetDao().getEntityById(loggedRoutineSetId)
    fun getLoggedRoutineSets() = database.getLoggedRoutineSetDao().getAllEntities()
    suspend fun getLoggedRoutineSetsList() = database.getLoggedRoutineSetDao().getAllEntitiesList()
    fun getLoggedRoutineSetsWithLoggedExerciseSets(loggedRoutineId: Long) = database.getLoggedRoutineSetDao().getLoggedRoutineSetsWithLoggedExerciseSets(loggedRoutineId)
    suspend fun getLoggedRouitneSetMaxOrder(loggedRoutineId: Long) = database.getLoggedRoutineSetDao().getMaxOrder(loggedRoutineId)
    suspend fun getLoggedRoutineSetsByRoutineId(loggedRoutineId: Long) = database.getLoggedRoutineSetDao().getLoggedRoutineSetsByRoutineId(loggedRoutineId)
    suspend fun getMaxSetsCountForExercise(exerciseId: Long) = database.getLoggedRoutineSetDao().getMaxSetsCountForExercise(exerciseId)
    suspend fun updateLoggedRoutineSetsUserUID(userUID: String) = database.getLoggedRoutineSetDao().updateLoggedRoutineSetsUserUID(userUID)

    // Logged Exercise Sets
    suspend fun upsertLoggedExerciseSet(loggedExerciseSet: LoggedExerciseSet) = database.getLoggedExerciseSetDao().upsert(loggedExerciseSet)
    suspend fun deleteLoggedExerciseSet(loggedExerciseSet: LoggedExerciseSet) = database.getLoggedExerciseSetDao().deleteEntity(loggedExerciseSet)
    suspend fun deleteAllLoggedExerciseSets() = database.getLoggedExerciseSetDao().deleteAllLoggedExerciseSets()
    fun getLoggedExerciseSets() = database.getLoggedExerciseSetDao().getAllEntities()
    suspend fun getExerciseSetsForLoggedRoutine(loggedRoutineId: Long) = database.getLoggedExerciseSetDao().getExerciseSetsForLoggedRoutine(loggedRoutineId)
    suspend fun getExerciseSetsForLoggedRoutineSet(loggedRoutineSetId: Long) = database.getLoggedExerciseSetDao().getExerciseSetsForLoggedRoutineSet(loggedRoutineSetId)
    suspend fun getLoggedExerciseSetsList() = database.getLoggedExerciseSetDao().getAllEntitiesList()
    suspend fun getLoggedExerciseSetMaxOrder(loggedRoutineSetId: Long) = database.getLoggedExerciseSetDao().getMaxOrder(loggedRoutineSetId)
    suspend fun getLoggedExerciseSetsWithDateMap() = database.getLoggedExerciseSetDao().getLoggedExerciseSetsWithDateMap()
    suspend fun getLoggedExerciseSetsWithDateMapByCategory(categoryId: Long) = database.getLoggedExerciseSetDao().getLoggedExerciseSetsWithDateMapByCategory(categoryId)
    suspend fun getLoggedExerciseSetsWithDateMapByExercise(exerciseId: Long) = database.getLoggedExerciseSetDao().getLoggedExerciseSetsWithDateMapByExercise(exerciseId)
    suspend fun getMaxWeightForExercise(exerciseId: Long) = database.getLoggedExerciseSetDao().getMaxWeightForExercise(exerciseId)
    suspend fun getMaxRepsForExercise(exerciseId: Long) = database.getLoggedExerciseSetDao().getMaxRepsForExercise(exerciseId)
    suspend fun updateLoggedExerciseSetsUserUID(userUID: String) = database.getLoggedExerciseSetDao().updateLoggedExerciseSetsUserUID(userUID)

    // Last Logged In User
    suspend fun upsertLastLoggedInUser(lastLoggedInUser: LastLoggedInUser) = database.getLastLoggedInUserDao().upsert(lastLoggedInUser)
    suspend fun deleteLastLoggedInUser(lastLoggedInUser: LastLoggedInUser) = database.getLastLoggedInUserDao().deleteEntity(lastLoggedInUser)
    fun getLastLoggedInUser() = database.getLastLoggedInUserDao().getAllEntities()
    suspend fun getLastLoggedInUserList() = database.getLastLoggedInUserDao().getAllEntitiesList()


    // Batch Functions
    suspend fun batchUpdateUserUIDs(userUID: String) {
        updateCategoriesUserUID(userUID)
        updateExercisesUserUID(userUID)
        updateWorkoutRoutinesUserUID(userUID)
        updateRoutineSetsUserUID(userUID)
        updateLoggedWorkoutRoutinesUserUID(userUID)
        updateLoggedRoutineSetsUserUID(userUID)
        updateLoggedExerciseSetsUserUID(userUID)
    }

    suspend fun nukeDatabase() {
        deleteAllCategories()
        deleteAllExercises()
        deleteAllWorkoutRoutines()
        deleteAllRoutineSets()
        deleteAllLoggedWorkoutRoutines()
        deleteAllLoggedRoutineSets()
        deleteAllLoggedExerciseSets()
    }
}