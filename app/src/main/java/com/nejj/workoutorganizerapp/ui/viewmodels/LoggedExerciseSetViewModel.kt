package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class LoggedExerciseSetViewModel(
    val workoutRepository: WorkoutRepository
) : ViewModel() {

    fun insertEntity(entity: LoggedExerciseSet) = viewModelScope.launch {
        workoutRepository.upsertLoggedExerciseSet(entity)
    }

    fun deleteEntity(entity: LoggedExerciseSet) = viewModelScope.launch {
        workoutRepository.deleteLoggedExerciseSet(entity)

        val loggedRoutineSet = workoutRepository.getLoggedRoutineSetById(entity.loggedRoutineSetId!!)
        loggedRoutineSet.setsCount--
        workoutRepository.upsertLoggedRoutineSet(loggedRoutineSet)
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

        loggedRoutineSet.setsCount++
        workoutRepository.upsertLoggedRoutineSet(loggedRoutineSet)
    }

    fun getMaxWeightForExercise(exerciseId: Long) : LiveData<Double>  {
        val maxWeight: MutableLiveData<Double> = MutableLiveData<Double>()


        viewModelScope.launch {
            val loggedRoutineSet = workoutRepository.getLoggedRoutineSetById(exerciseId)


            val maxWeightDate = workoutRepository.getMaxWeightForExercise(loggedRoutineSet.exerciseId!!)?.entries?.iterator()?.next()
            maxWeight.postValue(maxWeightDate?.value)
        }

        return maxWeight
    }
}