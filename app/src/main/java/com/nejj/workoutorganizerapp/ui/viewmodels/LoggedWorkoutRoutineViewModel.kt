package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.models.LoggedWorkoutRoutine
import com.nejj.workoutorganizerapp.models.relations.LoggedWorkoutRoutineWithLoggedRoutineSets
import com.nejj.workoutorganizerapp.models.relations.RoutineSetsWithExercise
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoggedWorkoutRoutineViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app) {

    private val _initializedLoggedWorkoutRoutineWithLoggedRoutineSets = MutableLiveData<LoggedWorkoutRoutineWithLoggedRoutineSets>()
    val initializedLoggedWorkoutRoutineWithLoggedRoutineSets: LiveData<LoggedWorkoutRoutineWithLoggedRoutineSets>
        get() = _initializedLoggedWorkoutRoutineWithLoggedRoutineSets

    fun insertEntity(loggedWorkoutRoutine: LoggedWorkoutRoutine)  = viewModelScope.launch {
        workoutRepository.upsertLoggedWorkoutRoutine(loggedWorkoutRoutine)
    }

    fun deleteEntity(loggedWorkoutRoutine: LoggedWorkoutRoutine) = viewModelScope.launch {
        workoutRepository.deleteLoggedWorkoutRoutine(loggedWorkoutRoutine)
    }

    fun getAllEntities() = workoutRepository.getLoggedWorkoutRoutines()

    fun getLoggedWorkoutRoutineWithLoggedRoutineSets() = workoutRepository.getLoggedWorkoutRoutineWithLoggedRoutineSets()

    fun initializeLoggedWorkoutRoutine(loggedWorkoutRoutine: LoggedWorkoutRoutine, routineSetsWithExercise: MutableList<RoutineSetsWithExercise>) {

        viewModelScope.launch {
            val loggedWorkoutRoutineId = workoutRepository.upsertLoggedWorkoutRoutine(loggedWorkoutRoutine)

            for(routineSet in routineSetsWithExercise) {

                // initialize logged routine sets from the routine sets loaded from the routine id
                // insert logged routine sets
                val loggedRoutineSet = LoggedRoutineSet(routineSet, loggedWorkoutRoutineId)
                val loggedRoutineSetId = workoutRepository.upsertLoggedRoutineSet(loggedRoutineSet)

                loggedRoutineSet.loggedRoutineSetId = loggedRoutineSetId
                // initialize exercise sets from the routine sets
                // insert exercise sets
                for(i in 1..loggedRoutineSet.warmupSetsCount) {
                    val loggedExerciseSet = LoggedExerciseSet(loggedRoutineSet, 0, true)
                    workoutRepository.upsertLoggedExerciseSet(loggedExerciseSet)
                }

                for(i in 1..loggedRoutineSet.setsCount) {
                    val loggedExerciseSet = LoggedExerciseSet(loggedRoutineSet, i, false)
                    workoutRepository.upsertLoggedExerciseSet(loggedExerciseSet)
                }
            }

            _initializedLoggedWorkoutRoutineWithLoggedRoutineSets.value = workoutRepository.getLogWorkoutWithSets(loggedWorkoutRoutineId)

        }
    }
}