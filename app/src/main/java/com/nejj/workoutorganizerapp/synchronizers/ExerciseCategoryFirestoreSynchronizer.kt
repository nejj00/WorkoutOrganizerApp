package com.nejj.workoutorganizerapp.synchronizers

import com.google.firebase.firestore.DocumentSnapshot
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository

class ExerciseCategoryFirestoreSynchronizer(workoutRepository: WorkoutRepository)
    : FirestoreSynchronizer<ExerciseCategory>(workoutRepository, "exercise_categories") {

    override suspend fun getLocalEntitiesList(): List<ExerciseCategory> {
        return workoutRepository.getUserMadeCategoriesList()
    }

    override fun getEntityId(entity: ExerciseCategory): Long {
        return entity.categoryId!!
    }

    override fun getIdFieldName(): String {
        return "categoryId"
    }

    override suspend fun upsertdDataToLocalDB(entity: ExerciseCategory) {
        workoutRepository.upsertCategory(entity)
    }

    override fun getMapFromEntity(entity: ExerciseCategory): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["categoryId"] = entity.categoryId!!
        map["name"] = entity.name
        map["isUserMade"] = entity.isUserMade
        map["userUID"] = entity.userUID ?: ""

        return map
    }

    override fun insertToFirestoreList(
        document: DocumentSnapshot,
        entitiesListFirestore: MutableList<ExerciseCategory>
    ) {
        entitiesListFirestore.add(document.toObject(ExerciseCategory::class.java)!!)
    }
}