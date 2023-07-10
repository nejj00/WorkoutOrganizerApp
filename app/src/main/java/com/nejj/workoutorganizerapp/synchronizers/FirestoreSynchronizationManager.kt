package com.nejj.workoutorganizerapp.synchronizers

import com.nejj.workoutorganizerapp.repositories.FirestoreRepository
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository

class FirestoreSynchronizationManager(workoutRepository: WorkoutRepository) {

    private val exerciseCategorySynchronizer = ExerciseFirestoreSynchronizer(workoutRepository)
    private val exerciseSynchronizer = ExerciseFirestoreSynchronizer(workoutRepository)
    private val workoutRoutineSynchronizer = WorkoutRoutineFirestoreSynchronizer(workoutRepository)
    private val routineSetSynchronizer = RoutineSetFirestoreSynchronizer(workoutRepository)
    private val loggedWorkoutRoutineSynchronizer = LoggedWorkoutRoutineFirestoreSynchronizer(workoutRepository)
    private val loggedRoutineSetSynchronizer = LoggedRoutineSetFirestoreSynchronizer(workoutRepository)
    private val loggedExerciseSynchronizer = LoggedExerciseSetFirestoreSynchronizer(workoutRepository)

    fun synchronize() {
        exerciseCategorySynchronizer.saveEntitiesToFirebase()
        exerciseSynchronizer.saveEntitiesToFirebase()
        workoutRoutineSynchronizer.saveEntitiesToFirebase()
        routineSetSynchronizer.saveEntitiesToFirebase()
        loggedWorkoutRoutineSynchronizer.saveEntitiesToFirebase()
        loggedRoutineSetSynchronizer.saveEntitiesToFirebase()
        loggedExerciseSynchronizer.saveEntitiesToFirebase()
    }

    fun deleteFirestoreData(userUID: String) {
        exerciseCategorySynchronizer.deleteEntitiesFirebase(userUID)
        exerciseSynchronizer.deleteEntitiesFirebase(userUID)
        workoutRoutineSynchronizer.deleteEntitiesFirebase(userUID)
        routineSetSynchronizer.deleteEntitiesFirebase(userUID)
        loggedWorkoutRoutineSynchronizer.deleteEntitiesFirebase(userUID)
        loggedRoutineSetSynchronizer.deleteEntitiesFirebase(userUID)
        loggedExerciseSynchronizer.deleteEntitiesFirebase(userUID)
    }

    suspend fun saveFirebaseDataToLocalDB(userUID: String) {
        exerciseCategorySynchronizer.insertFirebaseDataToLocalDB(userUID)
        exerciseSynchronizer.insertFirebaseDataToLocalDB(userUID)
        workoutRoutineSynchronizer.insertFirebaseDataToLocalDB(userUID)
        routineSetSynchronizer.insertFirebaseDataToLocalDB(userUID)
        loggedWorkoutRoutineSynchronizer.insertFirebaseDataToLocalDB(userUID)
        loggedRoutineSetSynchronizer.insertFirebaseDataToLocalDB(userUID)
        loggedExerciseSynchronizer.insertFirebaseDataToLocalDB(userUID)
    }
}