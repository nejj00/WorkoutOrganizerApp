package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.WorkoutRoutine
import com.nejj.workoutorganizerapp.models.relations.LoggedWorkoutRoutineWithLoggedRoutineSets
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutRoutineMainViewModel(
    app: Application,
    workoutRepository: WorkoutRepository
) : MainViewModel<WorkoutRoutine>(app, workoutRepository, "workout_routines") {

    private val _workoutRoutineID = MutableLiveData<Long>()
    val workoutRoutineID: LiveData<Long>
        get() = _workoutRoutineID

    fun insertEntity(entity: WorkoutRoutine) = viewModelScope.launch {
        workoutRepository.upsertWorkoutRoutine(entity)
    }

    fun insertEntityAndGetID(entity: WorkoutRoutine) = viewModelScope.launch {
        _workoutRoutineID.value = workoutRepository.upsertWorkoutRoutine(entity)
    }

    fun deleteEntity(entity: WorkoutRoutine) = viewModelScope.launch {
        val routineSets = workoutRepository.getRoutineSetsByRoutineId(entity.routineId!!)
        routineSets.forEach {routineSet ->
            workoutRepository.deleteRoutineSet(routineSet)
        }

        workoutRepository.deleteWorkoutRoutine(entity)
        //TODO: Delete Routine Sets related to this workout routine
    }

    fun updateWorkoutRoutinesUserUID(userUID: String) = viewModelScope.launch {
        workoutRepository.updateWorkoutRoutinesUserUID(userUID)
    }

    fun getEntities() = workoutRepository.getWorkoutRoutines()



    fun getWorkoutRoutineWithRoutineSets(routineId: Long) = workoutRepository.getWorkoutRoutineWithRoutineSets(routineId)

    fun getWorkoutRoutineWithExercises(routineId: Long) = workoutRepository.getWorkoutRoutineWithExercises(routineId)
    override val classToken: Class<WorkoutRoutine>
        get() = WorkoutRoutine::class.java

    override suspend fun getLocalEntitiesList(): List<WorkoutRoutine> {
        return workoutRepository.getUserMadeWorkoutRoutinesList()
    }

    override fun getIdFieldName(): String {
        return "routineId"
    }

    override fun insertToFirestoreList(
        document: DocumentSnapshot,
        entitiesListFirestore: MutableList<WorkoutRoutine>
    ) {
        entitiesListFirestore.add(document.toObject(classToken)!!)
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

    override fun getEntityId(entity: WorkoutRoutine): Long {
        return entity.routineId!!
    }
}