package com.nejj.workoutorganizerapp.models

import com.nejj.workoutorganizerapp.enums.OverallStatisticsType
import com.nejj.workoutorganizerapp.enums.StatisticsType
import java.io.Serializable


data class StatisticsDataSet(
    val overallStatisticsType: StatisticsType,
    val categories: List<String>,
    val dataSet: List<Any>
) : Serializable