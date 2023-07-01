package com.nejj.workoutorganizerapp.synchronizers

import com.google.firebase.firestore.DocumentSnapshot
import com.nejj.workoutorganizerapp.models.LoggedWorkoutRoutine
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class LoggedWorkoutRoutineFirestoreSynchronizer(workoutRepository: WorkoutRepository)
    : FirestoreSynchronizer<LoggedWorkoutRoutine>(workoutRepository, "logged_workout_routines") {

    override suspend fun getLocalEntitiesList(): List<LoggedWorkoutRoutine> {
        return workoutRepository.getLoggedWorkoutRoutinesList()
    }

    override fun getEntityId(entity: LoggedWorkoutRoutine): Long {
        return entity.loggedRoutineId!!
    }

    override fun getIdFieldName(): String {
        return "loggedRoutineId"
    }

    override suspend fun upsertdDataToLocalDB(entity: LoggedWorkoutRoutine) {
        workoutRepository.upsertLoggedWorkoutRoutine(entity)
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
}