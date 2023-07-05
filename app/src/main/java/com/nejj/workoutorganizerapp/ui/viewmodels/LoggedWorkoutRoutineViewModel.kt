package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.enums.ExerciseStatisticsType
import com.nejj.workoutorganizerapp.enums.OverallStatisticsType
import com.nejj.workoutorganizerapp.enums.StatisticsType
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.models.LoggedWorkoutRoutine
import com.nejj.workoutorganizerapp.models.relations.LoggedWorkoutRoutineWithLoggedRoutineSets
import com.nejj.workoutorganizerapp.models.relations.RoutineSetsWithExercise
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import com.nejj.workoutorganizerapp.util.StatisticsDataSetProcessor
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LoggedWorkoutRoutineViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app) {

    fun insertEntity(loggedWorkoutRoutine: LoggedWorkoutRoutine)  = viewModelScope.launch {
        workoutRepository.upsertLoggedWorkoutRoutine(loggedWorkoutRoutine)
    }

    suspend fun insertEntityAndGetID(loggedWorkoutRoutine: LoggedWorkoutRoutine) : Long {
        return workoutRepository.upsertLoggedWorkoutRoutine(loggedWorkoutRoutine)
    }

    fun deleteEntity(loggedWorkoutRoutine: LoggedWorkoutRoutine) = viewModelScope.launch {
        val loggedRoutineSets = workoutRepository.getLoggedRoutineSetsByRoutineId(loggedWorkoutRoutine.loggedRoutineId!!)
        loggedRoutineSets.forEach { loggedRoutineSet ->
            val loggedExerciseSets = workoutRepository.getExerciseSetsForLoggedRoutineSet(loggedRoutineSet.loggedRoutineSetId!!)
            loggedExerciseSets.forEach {
                workoutRepository.deleteLoggedExerciseSet(it)
            }

            workoutRepository.deleteLoggedRoutineSet(loggedRoutineSet)
        }

        workoutRepository.deleteLoggedWorkoutRoutine(loggedWorkoutRoutine)
        // TODO Delete Logged Routine Sets related to this Logged Workout Routine
    }

    fun updateLoggedWorkoutRoutinesUserUID(userUID: String) = viewModelScope.launch {
        workoutRepository.updateLoggedWorkoutRoutinesUserUID(userUID)
    }

    fun getAllEntities() = workoutRepository.getLoggedWorkoutRoutines()

    fun getLoggedWorkoutRoutineWithLoggedRoutineSetsLive() = workoutRepository.getLoggedWorkoutRoutineWithLoggedRoutineSetsLive()
    suspend fun getLoggedWorkoutRoutineWithLoggedRoutineSets() = workoutRepository.getLoggedWorkoutRoutineWithLoggedRoutineSets()

    suspend fun initializeLoggedWorkoutRoutine(loggedWorkoutRoutine: LoggedWorkoutRoutine, routineSetsWithExercise: List<RoutineSetsWithExercise>) : LoggedWorkoutRoutineWithLoggedRoutineSets {
        loggedWorkoutRoutine.userUID = Firebase.auth.currentUser?.uid
        val loggedWorkoutRoutineId = workoutRepository.upsertLoggedWorkoutRoutine(loggedWorkoutRoutine)

        for(routineSet in routineSetsWithExercise) {
            // initialize logged routine sets from the routine sets loaded from the routine id
            // insert logged routine sets
            val loggedRoutineSet = LoggedRoutineSet(routineSet, loggedWorkoutRoutineId)
            loggedRoutineSet.userUID = loggedWorkoutRoutine.userUID
            val loggedRoutineSetId = workoutRepository.upsertLoggedRoutineSet(loggedRoutineSet)
            loggedRoutineSet.loggedRoutineSetId = loggedRoutineSetId
            // initialize exercise sets from the routine sets
            // insert exercise sets
            for (i in 1..loggedRoutineSet.warmupSetsCount) {
                val loggedExerciseSet = LoggedExerciseSet(loggedRoutineSet, 0, true)
                loggedExerciseSet.userUID = loggedWorkoutRoutine.userUID
                workoutRepository.upsertLoggedExerciseSet(loggedExerciseSet)
            }
            for (i in 1..loggedRoutineSet.setsCount) {
                val loggedExerciseSet = LoggedExerciseSet(loggedRoutineSet, i, false)
                loggedExerciseSet.userUID = loggedWorkoutRoutine.userUID
                workoutRepository.upsertLoggedExerciseSet(loggedExerciseSet)
            }
        }

        return workoutRepository.getLogWorkoutWithSets(loggedWorkoutRoutineId)
    }
}