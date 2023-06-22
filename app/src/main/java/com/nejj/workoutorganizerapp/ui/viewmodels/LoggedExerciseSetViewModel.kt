package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class LoggedExerciseSetViewModel(
    app: Application,
    workoutRepository: WorkoutRepository
) : MainViewModel<LoggedExerciseSet>(app, workoutRepository, "logged_exercise_sets") {

    fun insertEntity(entity: LoggedExerciseSet) = viewModelScope.launch {
        workoutRepository.upsertLoggedExerciseSet(entity)
    }

    fun deleteEntity(entity: LoggedExerciseSet) = viewModelScope.launch {
        workoutRepository.deleteLoggedExerciseSet(entity)
    }

    fun updateLoggedExerciseSetsUserUID(userUID: String) = viewModelScope.launch {
        workoutRepository.updateLoggedExerciseSetsUserUID(userUID)
    }

    fun insertNewExerciseSetToLoggedRoutineSet(loggedRoutineSet: LoggedRoutineSet) = viewModelScope.launch {
        val maxOrder = workoutRepository.getLoggedExerciseSetMaxOrder(loggedRoutineSet.loggedRoutineSetId!!)?: 0

        val loggedExerciseSet = LoggedExerciseSet()
        loggedExerciseSet.loggedRoutineId = loggedRoutineSet.loggedRoutineId
        loggedExerciseSet.loggedRoutineSetId = loggedRoutineSet.loggedRoutineSetId
        loggedExerciseSet.setOrder = maxOrder + 1
        loggedExerciseSet.userUID = Firebase.auth.uid.toString()

        workoutRepository.upsertLoggedExerciseSet(loggedExerciseSet)
    }

    override val classToken: Class<LoggedExerciseSet>
        get() = LoggedExerciseSet::class.java

    override suspend fun getLocalEntitiesList(): List<LoggedExerciseSet> {
        return workoutRepository.getLoggedExerciseSetsList()
    }

    override fun getIdFieldName(): String {
        return "loggedExerciseSetId"
    }

    override fun insertToFirestoreList(
        document: DocumentSnapshot,
        entitiesListFirestore: MutableList<LoggedExerciseSet>
    ) {
        entitiesListFirestore.add(document.toObject(classToken)!!)
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

    override fun getEntityId(entity: LoggedExerciseSet): Long {
        return entity.loggedExerciseSetId!!
    }
}