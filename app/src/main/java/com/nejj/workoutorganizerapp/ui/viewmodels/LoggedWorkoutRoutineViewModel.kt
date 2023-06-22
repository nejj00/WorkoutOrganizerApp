package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.models.LoggedWorkoutRoutine
import com.nejj.workoutorganizerapp.models.relations.LoggedWorkoutRoutineWithLoggedRoutineSets
import com.nejj.workoutorganizerapp.models.relations.RoutineSetsWithExercise
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LoggedWorkoutRoutineViewModel(
    app: Application,
    workoutRepository: WorkoutRepository
) : MainViewModel<LoggedWorkoutRoutine>(app, workoutRepository, "logged_workout_routines") {

    private val _loggedWorkoutRoutineID = MutableLiveData<Long>()
    val loggedWorkoutRoutineID: LiveData<Long>
        get() = _loggedWorkoutRoutineID

    fun insertEntity(loggedWorkoutRoutine: LoggedWorkoutRoutine)  = viewModelScope.launch {
        workoutRepository.upsertLoggedWorkoutRoutine(loggedWorkoutRoutine)
    }

    fun insertEntityAndGetID(loggedWorkoutRoutine: LoggedWorkoutRoutine)  = viewModelScope.launch {
        _loggedWorkoutRoutineID.value = workoutRepository.upsertLoggedWorkoutRoutine(loggedWorkoutRoutine)
    }

    fun deleteEntity(loggedWorkoutRoutine: LoggedWorkoutRoutine) = viewModelScope.launch {
        val loggedRoutineSets = workoutRepository.getLoggedRoutineSetsByRoutineId(loggedWorkoutRoutine.loggedRoutineId!!)
        loggedRoutineSets.forEach { loggedRoutineSet ->
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

    suspend fun getVolumeStatisticsDataSetMap() : MutableMap<String, Float> {
        val workoutsWithRoutineSets = getLoggedWorkoutRoutineWithLoggedRoutineSets()
        val dateToExerciseSetsMap = mutableMapOf<LocalDate, List<LoggedExerciseSet>>()

        workoutsWithRoutineSets.forEach {
            val exerciseSets = workoutRepository.getExerciseSetsForLoggedRoutine(it.loggedWorkoutRoutine.loggedRoutineId!!)
            dateToExerciseSetsMap[it.loggedWorkoutRoutine.date] = exerciseSets
        }

        val dateVolumeMap = mutableMapOf<String, Float>()
        dateToExerciseSetsMap.forEach { (date, exerciseSetsList) ->
            var totalVolume = 0.0f
            exerciseSetsList.forEach {loggedExerciseSet ->
                val predicate: (LoggedExerciseSet) -> Boolean = {it.loggedRoutineSetId == loggedExerciseSet.loggedRoutineSetId}
                totalVolume += loggedExerciseSet.reps.toFloat() * loggedExerciseSet.weight.toFloat() * exerciseSetsList.count(predicate).toFloat()
            }

            dateVolumeMap[date.toString()] = totalVolume
        }

        return dateVolumeMap
    }

    override val classToken: Class<LoggedWorkoutRoutine>
        get() = LoggedWorkoutRoutine::class.java

    override suspend fun getLocalEntitiesList(): List<LoggedWorkoutRoutine> {
        return workoutRepository.getLoggedWorkoutRoutinesList()
    }

    override fun getIdFieldName(): String {
        return "loggedRoutineId"
    }

    override fun insertToFirestoreList(
        document: DocumentSnapshot,
        entitiesListFirestore: MutableList<LoggedWorkoutRoutine>
    ) {
        val loggedWorkoutRoutine = LoggedWorkoutRoutine()

        loggedWorkoutRoutine.loggedRoutineId =  document.data?.get("loggedRoutineId") as Long
        loggedWorkoutRoutine.name = document.data?.get("name") as String
        loggedWorkoutRoutine.bodyweight = document.data?.get("bodyweight") as Double
        loggedWorkoutRoutine.notes = document.data?.get("notes") as String
        loggedWorkoutRoutine.date = LocalDate.parse(document.data?.get("date") as String, DateTimeFormatter.ISO_LOCAL_DATE)
        loggedWorkoutRoutine.startTime = LocalTime.parse(document.data?.get("startTime") as String, DateTimeFormatter.ISO_LOCAL_TIME)

        val endTime = document.data?.get("endTime") as String
        if(endTime.isEmpty()) {
            loggedWorkoutRoutine.endTime = null
        } else {
            loggedWorkoutRoutine.endTime = LocalTime.parse(document.data?.get("endTime") as String, DateTimeFormatter.ISO_LOCAL_TIME)
        }

        loggedWorkoutRoutine.userUID = document.data?.get("userUID") as String

        entitiesListFirestore.add(loggedWorkoutRoutine)
    }

    override fun getMapFromEntity(entity: LoggedWorkoutRoutine): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["loggedRoutineId"] = entity.loggedRoutineId!!
        map["name"] = entity.name
        map["bodyweight"] = entity.bodyweight
        map["notes"] = entity.notes
        map["date"] = entity.date.toString()

        val localizedTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        map["startTime"] = entity.startTime.format(localizedTimeFormatter)

        if(entity.endTime != null)
            map["endTime"] = entity.endTime!!.format(localizedTimeFormatter)
        else
            map["endTime"] = ""

        map["userUID"] = entity.userUID ?: ""

        return map
    }

    override fun getEntityId(entity: LoggedWorkoutRoutine): Long {
        return entity.loggedRoutineId!!
    }
}