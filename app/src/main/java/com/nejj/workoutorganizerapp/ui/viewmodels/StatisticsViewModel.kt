package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nejj.workoutorganizerapp.enums.ExerciseStatisticsType
import com.nejj.workoutorganizerapp.enums.PersonalRecordStatisticsType
import com.nejj.workoutorganizerapp.enums.StatisticsType
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.PersonalRecord
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import com.nejj.workoutorganizerapp.util.StatisticsDataSetProcessor
import java.time.LocalDate

class StatisticsViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app) {

    private val statisticsDataSetProcessor = StatisticsDataSetProcessor()

    suspend fun getStatisticsDataSetMap(statisticType: StatisticsType): MutableMap<String, Any> {
        val loggedExerciseSetsWithDateMap = workoutRepository.getLoggedExerciseSetsWithDateMap()

        return getDataSetMap(loggedExerciseSetsWithDateMap, statisticType)
    }

    suspend fun getStatisticsDataSetMapForCategory(statisticType: StatisticsType, categoryId: Long) : MutableMap<String, Any> {
        val loggedExerciseSetsWithDateMap = workoutRepository.getLoggedExerciseSetsWithDateMapByCategory(categoryId)

        return getDataSetMap(loggedExerciseSetsWithDateMap, statisticType)
    }

    suspend fun getLoggedExerciseSetsWithDateMapByExercise(statisticType: StatisticsType, exerciseId: Long) : MutableMap<String, Any> {
        val loggedExerciseSetsWithDateMap = workoutRepository.getLoggedExerciseSetsWithDateMapByExercise(exerciseId)

        return getDataSetMap(loggedExerciseSetsWithDateMap, statisticType)
    }

    suspend fun getLoggedExerciseSetsWithDateMapByExerciseDistinct(statisticType: StatisticsType, exerciseId: Long) : MutableMap<String, Any> {
        val loggedExerciseSetsWithDateMap = workoutRepository.getLoggedExerciseSetsWithDateMapByExercise(exerciseId)

        val dataSetMap = getDataSetMap(loggedExerciseSetsWithDateMap, statisticType)

        val distinctValuesMap = dataSetMap
            .entries
            .distinctBy { it.value }
            .associate { it.key to it.value }

        return distinctValuesMap.toMutableMap()
    }

    private fun getDataSetMap(
        loggedExerciseSetsWithDateMap: Map<LoggedExerciseSet, LocalDate>,
        statisticType: StatisticsType
    ): MutableMap<String, Any> {
        val exerciseSetsByDate =
            loggedExerciseSetsWithDateMap.entries.groupBy({ it.value }, { it.key })

        return statisticsDataSetProcessor.getOverallDataSetMapByStatisticType(
            statisticType,
            exerciseSetsByDate
        )
    }

    suspend fun getPersonalRecordsForExercise(exerciseId: Long): MutableList<PersonalRecord> {
        val maxWeight = workoutRepository.getMaxWeightForExercise(exerciseId)?.entries?.iterator()?.next()
        val maxReps = workoutRepository.getMaxRepsForExercise(exerciseId)?.entries?.iterator()?.next()
        val maxSets = workoutRepository.getMaxSetsCountForExercise(exerciseId)?.entries?.iterator()?.next()

        val maxWeightNum = maxWeight?.value
        val maxWeightPersonalRecord = PersonalRecord(PersonalRecordStatisticsType.MAX_WEIGHT, maxWeightNum?: 0.0, maxWeight?.key?: LocalDate.MIN, exerciseId)
        val maxRepsNum = maxReps?.value?.toDouble()
        val maxRepsPersonalRecord = PersonalRecord(PersonalRecordStatisticsType.MAX_REPS, maxRepsNum?: 0.0, maxReps?.key?: LocalDate.MIN, exerciseId)
        val maxSetsNum = maxSets?.value?.toDouble()
        val maxSetsPersonalRecord = PersonalRecord(PersonalRecordStatisticsType.TOTAL_SETS, maxSetsNum?: 0.0, maxSets?.key?: LocalDate.MIN, exerciseId)

        return mutableListOf(maxWeightPersonalRecord, maxRepsPersonalRecord, maxSetsPersonalRecord)
    }

    suspend fun getEstimatedOneRepMaxForExercise(exerciseId: Long): MutableMap<String, Any> {
        val loggedExerciseSetsWithDateMap = workoutRepository.getLoggedExerciseSetsWithDateMapByExercise(exerciseId)

        return statisticsDataSetProcessor.getEstimatedOneRepMaxOverTime(loggedExerciseSetsWithDateMap)
    }
}