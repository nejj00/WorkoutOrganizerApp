package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.models.RoutineSet
import com.nejj.workoutorganizerapp.models.WorkoutRoutine
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class RoutineSetMainViewModel(
    app: Application,
    workoutRepository: WorkoutRepository
) : MainViewModel<RoutineSet>(app, workoutRepository, "routine_sets"){

    fun insertEntity(entity: RoutineSet) = viewModelScope.launch {
        workoutRepository.upsertRoutineSet(entity)
    }

    fun deleteEntity(entity: RoutineSet) = viewModelScope.launch {
        workoutRepository.deleteRoutineSet(entity)
    }
    suspend fun getRoutineSetsById(routineSetId: Long) = workoutRepository.getRoutineSetById(routineSetId)
    fun getEntities() = workoutRepository.getRoutineSets()

    suspend fun getRoutineSetsWithExercise(routineId: Long) = workoutRepository.getRoutineSetsWithExercise(routineId)
    fun getRoutineSetsWithExerciseLive(routineId: Long) = workoutRepository.getRoutineSetsWithExerciseLive(routineId)

    fun insertRoutineSet(routineId: Long, exerciseId: Long) =  viewModelScope.launch {

        val maxOrder = workoutRepository.getRoutineSetMaxOrder(routineId)?: 0
        workoutRepository.upsertRoutineSet(
            RoutineSet(null, routineId, exerciseId, 0, 1, maxOrder + 1, true, Firebase.auth.currentUser?.uid)
        )
    }

    fun updateRoutineSetsUserUID(userUID: String) = viewModelScope.launch {
        workoutRepository.updateRoutineSetsUserUID(userUID)
    }

    suspend fun getRoutineSetsByRoutineId(routineId: Long) = workoutRepository.getRoutineSetsByRoutineId(routineId)

    fun getRoutineSetsListByRoutineId(routineId: Long) = workoutRepository.getRoutineSetsListByRoutineId(routineId)

    override val classToken: Class<RoutineSet>
        get() = RoutineSet::class.java

    override suspend fun getLocalEntitiesList(): List<RoutineSet> {
        return workoutRepository.getUserMadeRoutineSetsList()
    }

    override fun getIdFieldName(): String {
        return "routineSetId"
    }

    override fun insertToFirestoreList(
        document: DocumentSnapshot,
        entitiesListFirestore: MutableList<RoutineSet>
    ) {
        entitiesListFirestore.add(document.toObject(classToken)!!)
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

    override fun getEntityId(entity: RoutineSet): Long {
        return entity.routineSetId!!
    }
}