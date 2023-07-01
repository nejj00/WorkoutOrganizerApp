package com.nejj.workoutorganizerapp.synchronizers

import com.google.firebase.firestore.DocumentSnapshot
import com.nejj.workoutorganizerapp.models.WorkoutRoutine
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository

class WorkoutRoutineFirestoreSynchronizer(workoutRepository: WorkoutRepository)
    : FirestoreSynchronizer<WorkoutRoutine>(workoutRepository, "workout_routines") {
    override suspend fun getLocalEntitiesList(): List<WorkoutRoutine> {
        return workoutRepository.getUserMadeWorkoutRoutinesList()
    }

    override fun getEntityId(entity: WorkoutRoutine): Long {
        return entity.routineId!!
    }

    override fun getIdFieldName(): String {
        return "routineId"
    }

    override suspend fun upsertdDataToLocalDB(entity: WorkoutRoutine) {
        workoutRepository.upsertWorkoutRoutine(entity)
    }

    override fun getMapFromEntity(entity: WorkoutRoutine): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["routineId"] = entity.routineId!!
        map["name"] = entity.name
        map["notes"] = entity.notes
        map["isUserMade"] = entity.isUserMade
        map["userUID"] = entity.userUID ?: ""

        return map
    }

    override fun insertToFirestoreList(
        document: DocumentSnapshot,
        entitiesListFirestore: MutableList<WorkoutRoutine>
    ) {
        entitiesListFirestore.add(document.toObject(WorkoutRoutine::class.java)!!)
    }
}