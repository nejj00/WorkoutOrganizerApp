package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.WorkoutRoutine
import com.nejj.workoutorganizerapp.models.relations.LoggedWorkoutRoutineWithLoggedRoutineSets
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class WorkoutRoutineMainViewModel(
    val workoutRepository: WorkoutRepository
) : ViewModel() {

    fun insertEntity(entity: WorkoutRoutine) = viewModelScope.launch {
        workoutRepository.upsertWorkoutRoutine(entity)
    }

    fun insertEntityAndGetID(entity: WorkoutRoutine) : LiveData<Long> {
        val workoutRoutineID: MutableLiveData<Long> =  MutableLiveData()

        viewModelScope.launch {
            workoutRoutineID.value = workoutRepository.upsertWorkoutRoutine(entity)
        }

        return workoutRoutineID
    }

    fun deleteEntity(entity: WorkoutRoutine) = viewModelScope.launch {
        val routineSets = workoutRepository.getRoutineSetsByRoutineId(entity.routineId!!)
        routineSets.forEach {routineSet ->
            workoutRepository.deleteRoutineSet(routineSet)
        }

        workoutRepository.deleteWorkoutRoutine(entity)
    }

    fun updateWorkoutRoutinesUserUID(userUID: String) = viewModelScope.launch {
        workoutRepository.updateWorkoutRoutinesUserUID(userUID)
    }

    fun getEntities() = workoutRepository.getWorkoutRoutines()

    fun getWorkoutRoutineWithRoutineSets(routineId: Long) = workoutRepository.getWorkoutRoutineWithRoutineSets(routineId)

    fun getWorkoutRoutineWithExercises(routineId: Long) = workoutRepository.getWorkoutRoutineWithExercises(routineId)
}