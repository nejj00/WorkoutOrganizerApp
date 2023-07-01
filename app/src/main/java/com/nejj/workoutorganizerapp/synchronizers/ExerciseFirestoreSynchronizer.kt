package com.nejj.workoutorganizerapp.synchronizers

import com.google.firebase.firestore.DocumentSnapshot
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository

class ExerciseFirestoreSynchronizer(workoutRepository: WorkoutRepository)
    : FirestoreSynchronizer<Exercise>(workoutRepository, "exercises") {
    override suspend fun getLocalEntitiesList(): List<Exercise> {
        return workoutRepository.getUserMadeExercisesList()
    }

    override fun getIdFieldName(): String {
        return "exerciseId"
    }

    override suspend fun upsertdDataToLocalDB(entity: Exercise) {
        workoutRepository.upsertExercise(entity)
    }

    override fun insertToFirestoreList(
        document: DocumentSnapshot,
        entitiesListFirestore: MutableList<Exercise>
    ) {
        entitiesListFirestore.add(document.toObject(Exercise::class.java)!!)
    }

    override fun getMapFromEntity(entity: Exercise): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["exerciseId"] = entity.exerciseId!!
        map["name"] = entity.name
        map["categoryId"] = entity.categoryId!!
        map["type"] = entity.type
        map["isSingleSide"] = entity.isSingleSide
        map["isUserMade"] = entity.isUserMade
        map["userUID"] = entity.userUID ?: ""

        return map
    }

    override fun getEntityId(entity: Exercise): Long {
        return entity.exerciseId!!
    }
}