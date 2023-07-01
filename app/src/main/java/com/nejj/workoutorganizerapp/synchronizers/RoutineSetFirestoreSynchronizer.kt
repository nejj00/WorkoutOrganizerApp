package com.nejj.workoutorganizerapp.synchronizers

import com.google.firebase.firestore.DocumentSnapshot
import com.nejj.workoutorganizerapp.models.RoutineSet
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository

class RoutineSetFirestoreSynchronizer(workoutRepository: WorkoutRepository)
    : FirestoreSynchronizer<RoutineSet>(workoutRepository, "routine_sets") {
    override suspend fun getLocalEntitiesList(): List<RoutineSet> {
        return workoutRepository.getUserMadeRoutineSetsList()
    }

    override fun getEntityId(entity: RoutineSet): Long {
        return entity.routineSetId!!
    }

    override fun getIdFieldName(): String {
        return "routineSetId"
    }

    override suspend fun upsertdDataToLocalDB(entity: RoutineSet) {
        workoutRepository.upsertRoutineSet(entity)
    }

    override fun getMapFromEntity(entity: RoutineSet): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["routineSetId"] = entity.routineSetId!!
        map["routineId"] = entity.routineId!!
        map["exerciseId"] = entity.exerciseId!!
        map["warmupSetsCount"] = entity.warmupSetsCount
        map["setsCount"] = entity.setsCount
        map["setsOrder"] = entity.setsOrder
        map["isUserMade"] = entity.isUserMade
        map["userUID"] = entity.userUID ?: ""

        return map
    }

    override fun insertToFirestoreList(
        document: DocumentSnapshot,
        entitiesListFirestore: MutableList<RoutineSet>
    ) {
        entitiesListFirestore.add(document.toObject(RoutineSet::class.java)!!)
    }
}