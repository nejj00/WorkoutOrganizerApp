package com.nejj.workoutorganizerapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.nejj.workoutorganizerapp.enums.OverallStatisticsType
import com.nejj.workoutorganizerapp.enums.PersonalRecordStatisticsType
import com.nejj.workoutorganizerapp.enums.StatisticsType
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.PersonalRecord
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import com.nejj.workoutorganizerapp.util.StatisticsDataSetProcessor
import java.time.LocalDate

class StatisticsViewModel(
    val workoutRepository: WorkoutRepository
) : ViewModel() {

    private val statisticsDataSetProcessor = StatisticsDataSetProcessor()

    suspend fun getStatisticsDataSetMap(statisticType: StatisticsType): MutableMap<String, Any> {
        val loggedExerciseSetsWithDateMap = workoutRepository.getLoggedExerciseSetsWithDateMap()

        if (statisticType.toString() == OverallStatisticsType.BODYWEIGHT_OVERTIME.toString())
            return getBodyweightOvertime()

        if (statisticType.toString() == OverallStatisticsType.NUMBER_OF_WORKOUTS.toString())
            return getWorkoutsForWeek()

        return getDataSetMap(loggedExerciseSetsWithDateMap, statisticType)
    }

    suspend fun getStatisticsDataSetMapForCategory(statisticType: StatisticsType, categoryId: Long) : MutableMap<String, Any> {
        val loggedExerciseSetsWithDateMap = workoutRepository.getLoggedExerciseSetsWithDateMapByCategory(categoryId)

        if(statisticType.toString() == OverallStatisticsType.BODYWEIGHT_OVERTIME.toString())
            return getBodyweightOvertime()

        return getDataSetMap(loggedExerciseSetsWithDateMap, statisticType)
    }

    suspend fun getLoggedExerciseSetsWithDateMapByExercise(statisticType: StatisticsType, exerciseId: Long) : MutableMap<String, Any> {
        val loggedExerciseSetsWithDateMap = workoutRepository.getLoggedExerciseSetsWithDateMapByExercise(exerciseId)

        if(statisticType.toString() == OverallStatisticsType.BODYWEIGHT_OVERTIME.toString())
            return getBodyweightOvertime()

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

        val dataSetMap: MutableMap<String, Any> = statisticsDataSetProcessor.getOverallDataSetMapByStatisticType(
            statisticType,
            exerciseSetsByDate
        ).filterValues { it != null }.toMutableMap() as MutableMap<String, Any>

        return dataSetMap
    }

    suspend fun getPersonalRecordsForExercise(exerciseId: Long): MutableList<PersonalRecord> {
        val maxWeight = workoutRepository.getMaxWeightForExercise(exerciseId)?.entries?.iterator()?.next()
        val maxReps = workoutRepository.getMaxRepsForExercise(exerciseId)?.entries?.iterator()?.next()
        val maxSets = workoutRepository.getMaxSetsCountForExercise(exerciseId)?.entries?.iterator()?.next()

        val maxWeightPersonalRecord = PersonalRecord(PersonalRecordStatisticsType.MAX_WEIGHT, maxWeight?.value?: 0.0, maxWeight?.key?: LocalDate.MIN, exerciseId)
        val maxRepsPersonalRecord = PersonalRecord(PersonalRecordStatisticsType.MAX_REPS, maxReps?.value?: 0, maxReps?.key?: LocalDate.MIN, exerciseId)
        val maxSetsPersonalRecord = PersonalRecord(PersonalRecordStatisticsType.TOTAL_SETS, maxSets?.value?: 0, maxSets?.key?: LocalDate.MIN, exerciseId)

        return mutableListOf(maxWeightPersonalRecord, maxRepsPersonalRecord, maxSetsPersonalRecord)
    }

    suspend fun getEstimatedOneRepMaxForExercise(exerciseId: Long): MutableMap<String, Any> {
        val loggedExerciseSetsWithDateMap = workoutRepository.getLoggedExerciseSetsWithDateMapByExercise(exerciseId)

        return statisticsDataSetProcessor.getEstimatedOneRepMaxOverTime(loggedExerciseSetsWithDateMap)
    }

    suspend fun getBodyweightOvertime(): MutableMap<String, Any> {
        val bodyweightDateMap = workoutRepository.getBodyweightOvertime()

        return statisticsDataSetProcessor.getBodyweightOverTime(bodyweightDateMap)
    }

    suspend fun getWorkoutsForWeek(): MutableMap<String, Any> {
        val workoutsForWeek = workoutRepository.getLoggedWorkoutRoutinesList()

        return statisticsDataSetProcessor.getWorkoutsForWeek(workoutsForWeek)
    }
}