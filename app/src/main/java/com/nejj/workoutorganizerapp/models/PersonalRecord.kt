package com.nejj.workoutorganizerapp.models

import com.nejj.workoutorganizerapp.enums.ExerciseStatisticsType
import com.nejj.workoutorganizerapp.enums.PersonalRecordStatisticsType
import java.io.Serializable
import java.time.LocalDate

data class PersonalRecord(
    val personalRecordStatisticsType: PersonalRecordStatisticsType,
    val record: Double,
    val date: LocalDate,
    val exerciseId: Long
) : Serializable
