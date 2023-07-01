package com.nejj.workoutorganizerapp.enums

enum class OverallStatisticsType(private val statisticsName: String) : StatisticsType {
    VOLUME("Volume"),
    TOTAL_REPS("Total Reps"),
    TOTAL_SETS("Total Sets"),
    REPS_PER_SET("Reps per Set"),
    NUMBER_OF_WORKOUTS("Number of Workouts"),
    MUSCLE_RANKING_CHART("Muscle Ranking Chart");

    override fun toString() = statisticsName

    override fun equals(other: StatisticsType): Boolean {
        return this.toString() == other.toString()
    }
}