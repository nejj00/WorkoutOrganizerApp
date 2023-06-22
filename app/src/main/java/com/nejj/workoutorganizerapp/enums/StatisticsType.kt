package com.nejj.workoutorganizerapp.enums

enum class StatisticsType(private val statisticsName: String) {
    VOLUME("Volume"),
    TOTAL_REPS("Total Reps"),
    TOTAL_SETS("Total Sets"),
    REPS_PER_SET("Reps per Set"),
    NUMBER_OF_WORKOUTS("Number of Workouts");

    override fun toString() = statisticsName
}