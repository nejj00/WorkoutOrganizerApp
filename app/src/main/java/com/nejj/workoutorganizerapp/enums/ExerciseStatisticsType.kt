package com.nejj.workoutorganizerapp.enums

enum class ExerciseStatisticsType(private val statisticsName: String) : StatisticsType {
    VOLUME("Volume"),
    TOTAL_REPS("Total Reps"),
    TOTAL_SETS("Total Sets"),
    REPS_PER_SET("Reps per Set"),
    NUMBER_OF_WORKOUTS("Number of Workouts"),
    AVG_WEIGHT("Average Weight"),
    EST_ONE_REP_MAX("Estimated One Rep Max"),
    PERSONAL_RECORDS("Personal Records");

    override fun toString() = statisticsName

    override fun equals(other: StatisticsType): Boolean {
        return this.toString() == other.toString()
    }
}