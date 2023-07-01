package com.nejj.workoutorganizerapp.synchronizers

import com.google.firebase.firestore.DocumentSnapshot
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository

class LoggedExerciseSetFirestoreSynchronizer(workoutRepository: WorkoutRepository)
    : FirestoreSynchronizer<LoggedExerciseSet>(workoutRepository, "logged_exercise_sets") {

    override suspend fun getLocalEntitiesList(): List<LoggedExerciseSet> {
        return workoutRepository.getLoggedExerciseSetsList()
    }

    override fun getEntityId(entity: LoggedExerciseSet): Long {
        return entity.loggedExerciseSetId!!
    }

    override fun getIdFieldName(): String {
        return "loggedExerciseSetId"
    }

    override suspend fun upsertdDataToLocalDB(entity: LoggedExerciseSet) {
        workoutRepository.upsertLoggedExerciseSet(entity)
    }

    override fun getMapFromEntity(entity: LoggedExerciseSet): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["loggedExerciseSetId"] = entity.loggedExerciseSetId!!
        map["loggedRoutineId"] = entity.loggedRoutineId!!
        map["loggedRoutineSetId"] = entity.loggedRoutineSetId!!
        map["weight"] = entity.weight
        map["reps"] = entity.reps
        map["setOrder"] = entity.setOrder
        map["isWarmupSet"] = entity.isWarmupSet
        map["notes"] = entity.notes
        map["userUID"] = entity.userUID ?: ""

        return map
    }

    override fun insertToFirestoreList(
        document: DocumentSnapshot,
        entitiesListFirestore: MutableList<LoggedExerciseSet>
    ) {
        entitiesListFirestore.add(document.toObject(LoggedExerciseSet::class.java)!!)
    }
}