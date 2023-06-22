package com.nejj.workoutorganizerapp.models

import java.io.Serializable


data class StatisticsDataSet(
    val categories: List<String>,
    val dataSet: List<Float>
) : Serializable