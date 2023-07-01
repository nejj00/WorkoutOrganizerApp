package com.nejj.workoutorganizerapp.synchronizers

import com.google.firebase.firestore.DocumentSnapshot
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository

class LoggedRoutineSetFirestoreSynchronizer(workoutRepository: WorkoutRepository)
    : FirestoreSynchronizer<LoggedRoutineSet>(workoutRepository, "logged_routine_sets") {
    override suspend fun getLocalEntitiesList(): List<LoggedRoutineSet> {
        return workoutRepository.getLoggedRoutineSetsList()
    }

    override fun getEntityId(entity: LoggedRoutineSet): Long {
        return entity.loggedRoutineSetId!!
    }

    override fun getIdFieldName(): String {
        return "loggedRoutineSetId"
    }

    override suspend fun upsertdDataToLocalDB(entity: LoggedRoutineSet) {
        workoutRepository.upsertLoggedRoutineSet(entity)
    }

    override fun getMapFromEntity(entity: LoggedRoutineSet): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["loggedRoutineSetId"] = entity.loggedRoutineSetId!!
        map["loggedRoutineId"] = entity.loggedRoutineId!!
        map["exerciseId"] = entity.exerciseId!!
        map["exerciseName"] = entity.exerciseName
        map["warmupSetsCount"] = entity.warmupSetsCount
        map["setsCount"] = entity.setsCount
        map["setsOrder"] = entity.setsOrder
        map["userUID"] = entity.userUID ?: ""

        return map
    }

    override fun insertToFirestoreList(
        document: DocumentSnapshot,
        entitiesListFirestore: MutableList<LoggedRoutineSet>
    ) {
        entitiesListFirestore.add(document.toObject(LoggedRoutineSet::class.java)!!)
    }
}