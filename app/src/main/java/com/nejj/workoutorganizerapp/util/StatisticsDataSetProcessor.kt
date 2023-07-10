package com.nejj.workoutorganizerapp.util

import com.nejj.workoutorganizerapp.enums.ExerciseStatisticsType
import com.nejj.workoutorganizerapp.enums.OverallStatisticsType
import com.nejj.workoutorganizerapp.enums.PersonalRecordStatisticsType
import com.nejj.workoutorganizerapp.enums.StatisticsType
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.LoggedWorkoutRoutine
import java.math.RoundingMode
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

class StatisticsDataSetProcessor {

    fun getOverallDataSetMapByStatisticType(
        statisticsType: StatisticsType,
        dateToExerciseSetsMap: Map<LocalDate, List<LoggedExerciseSet>>)
    : MutableMap<String, Any?> {

        val dateVolumeMap = mutableMapOf<String, Any?>()

        dateToExerciseSetsMap.forEach { (date, exerciseSetsList) ->
            var total = 0.0f

//            if(statisticsType == OverallStatisticsType.NUMBER_OF_WORKOUTS)
//                //total++
//            else
//                total = getDataForTotal(statisticsType, exerciseSetsList)

            dateVolumeMap[date.toString()] = getDataForTotal(statisticsType, exerciseSetsList)
        }

        return dateVolumeMap
    }

    private fun getDataForTotal(statisticsType: StatisticsType, loggedExerciseSets: List<LoggedExerciseSet>) : Float? {
        var data = 0.0f

        when(statisticsType.toString()) {
            OverallStatisticsType.VOLUME.toString() -> {
                loggedExerciseSets.forEach {loggedExerciseSet ->
                    val predicate: (LoggedExerciseSet) -> Boolean = {it.loggedRoutineSetId == loggedExerciseSet.loggedRoutineSetId}
                    data += loggedExerciseSet.reps.toFloat() * loggedExerciseSet.weight.toFloat() //* exerciseSetsList.count(predicate).toFloat()
                }
            }
            OverallStatisticsType.TOTAL_REPS.toString() -> {
                data = loggedExerciseSets.sumOf { it.reps  }.toFloat()
            }
            OverallStatisticsType.TOTAL_SETS.toString() -> {
                data = loggedExerciseSets.count{ set -> set.reps != 0 || set.weight != 0.0 }.toFloat()
            }
            OverallStatisticsType.REPS_PER_SET.toString() -> {
                val result = loggedExerciseSets.filter { set -> set.reps != 0 || set.weight != 0.0  }.map { it.reps }.average()

                data = if (result.isNaN())
                    0.0f
                else
                    result.toFloat()
            }
//            OverallStatisticsType.MUSCLE_RANKING_CHART.toString() -> {
//                loggedExerciseSets.forEach {loggedExerciseSet ->
//                    val predicate: (LoggedExerciseSet) -> Boolean = {it.loggedRoutineSetId == loggedExerciseSet.loggedRoutineSetId}
//                    data += loggedExerciseSet.reps.toFloat() * loggedExerciseSet.weight.toFloat() //* exerciseSetsList.count(predicate).toFloat()
//                }
//            }
            PersonalRecordStatisticsType.MAX_WEIGHT.toString() -> {
                data = loggedExerciseSets.maxOf { it.weight }.toFloat()
            }
            PersonalRecordStatisticsType.MAX_REPS.toString() -> {
                data = loggedExerciseSets.maxOf { it.reps }.toFloat()
            }
            ExerciseStatisticsType.AVG_WEIGHT.toString() -> {
                 val result = loggedExerciseSets.filter { it.weight != 0.0 }
                    .map { it.weight }
                    .average()

                data = if (result.isNaN())
                    0.0f
                else
                    result.toFloat()
            }

            else -> {}
        }

        return if(data == 0.0f) null else data
    }

    fun getEstimatedOneRepMaxOverTime(loggedExerciseSetsWithDateMap: Map<LoggedExerciseSet, LocalDate>): MutableMap<String, Any> {
        val filteredEntries = loggedExerciseSetsWithDateMap.entries.filter { entry ->
            val loggedExerciseSet = entry.key
            loggedExerciseSet.reps in 3..10
        }

        val estimatedOneRepMaxOverTime = mutableMapOf<String, Any>()
        filteredEntries.forEach {
            val loggedExerciseSet = it.key
            val date = it.value
            val estimatedOneRepMax = loggedExerciseSet.weight * (1 + (0.0333 * loggedExerciseSet.reps))

            estimatedOneRepMaxOverTime[date.toString()] = estimatedOneRepMax.toBigDecimal().setScale(2, RoundingMode.HALF_EVEN).toFloat()
        }

        return estimatedOneRepMaxOverTime
    }

    fun getBodyweightOverTime(bodyweightWithDateMap: Map<LocalDate, Double>): MutableMap<String, Any> {
        if(bodyweightWithDateMap.isEmpty())
            return mutableMapOf()

        val bodyweightOverTime = bodyweightWithDateMap.filter { it.value != 0.0 }.mapKeys { (key, _) -> key.toString() }

        return bodyweightOverTime.toMutableMap()
    }

    fun getWorkoutsForWeek(loggedWorkoutRoutines: List<LoggedWorkoutRoutine>): MutableMap<String, Any> {


        val weekPeriodMap = loggedWorkoutRoutines
            .groupBy { getWeekPeriod(it.date) }
            .mapValues { (_, workouts) -> workouts.size }

        return weekPeriodMap.toMutableMap()
    }

    private fun getWeekPeriod(date: LocalDate): String {
        val weekFields = WeekFields.of(Locale.getDefault())
        val weekNumber = date.get(weekFields.weekOfWeekBasedYear())
        val year = date.get(weekFields.weekBasedYear())
        return "Week $weekNumber ($year)"
    }
}