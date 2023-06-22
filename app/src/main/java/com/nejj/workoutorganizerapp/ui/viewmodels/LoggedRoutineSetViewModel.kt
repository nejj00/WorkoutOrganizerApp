package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.models.relations.LoggedRoutineSetWithLoggedExerciseSet
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class LoggedRoutineSetViewModel(
    app: Application,
    workoutRepository: WorkoutRepository
) : MainViewModel<LoggedRoutineSet>(app, workoutRepository, "logged_routine_sets") {

    fun insertEntity(entity: LoggedRoutineSet) = viewModelScope.launch {
        workoutRepository.upsertLoggedRoutineSet(entity)
    }

    fun deleteEntity(entity: LoggedRoutineSetWithLoggedExerciseSet) = viewModelScope.launch {
        entity.loggedExerciseSets.forEach { loggedExerciseSet ->
            workoutRepository.deleteLoggedExerciseSet(loggedExerciseSet)
        }

        workoutRepository.deleteLoggedRoutineSet(entity.loggedRoutineSet)
    }

    fun updateLoggedRoutineSetsUserUID(userUID: String) = viewModelScope.launch {
        workoutRepository.updateLoggedRoutineSetsUserUID(userUID)
    }

    fun insertNewLoggedRoutineSet(loggedRoutineId: Long, exercise: Exercise) = viewModelScope.launch {
        val maxOrder = workoutRepository.getLoggedRouitneSetMaxOrder(loggedRoutineId)?: 0

        val loggedRoutineSet = LoggedRoutineSet()
        loggedRoutineSet.loggedRoutineId = loggedRoutineId
        loggedRoutineSet.exerciseId = exercise.exerciseId
        loggedRoutineSet.exerciseName = exercise.name
        loggedRoutineSet.setsOrder = maxOrder + 1
        loggedRoutineSet.setsCount = 1
        loggedRoutineSet.userUID = Firebase.auth.currentUser?.uid

        val loggedRoutineSetId = workoutRepository.upsertLoggedRoutineSet(loggedRoutineSet)

        val loggedExerciseSet = LoggedExerciseSet()
        loggedExerciseSet.loggedRoutineId = loggedRoutineId
        loggedExerciseSet.loggedRoutineSetId = loggedRoutineSetId
        loggedExerciseSet.userUID = Firebase.auth.currentUser?.uid

        workoutRepository.upsertLoggedExerciseSet(loggedExerciseSet)
    }

    fun getLoggedRoutineSetsWithLoggedExerciseSets(loggedRoutineId: Long) = workoutRepository.getLoggedRoutineSetsWithLoggedExerciseSets(loggedRoutineId)

    suspend fun getLoggedRoutineSetsByLoggedRoutineId(loggedRoutineId: Long) = workoutRepository.getLoggedRoutineSetsByRoutineId(loggedRoutineId)

    override val classToken: Class<LoggedRoutineSet>
        get() = LoggedRoutineSet::class.java

    override suspend fun getLocalEntitiesList(): List<LoggedRoutineSet> {
        return workoutRepository.getLoggedRoutineSetsList()
    }

    override fun getIdFieldName(): String {
        return "loggedRoutineSetId"
    }

    override fun insertToFirestoreList(
        document: DocumentSnapshot,
        entitiesListFirestore: MutableList<LoggedRoutineSet>
    ) {
        entitiesListFirestore.add(document.toObject(classToken)!!)
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

    override fun getEntityId(entity: LoggedRoutineSet): Long {
        return entity.loggedRoutineSetId!!
    }
}