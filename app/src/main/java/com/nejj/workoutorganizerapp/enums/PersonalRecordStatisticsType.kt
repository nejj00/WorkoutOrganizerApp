package com.nejj.workoutorganizerapp.enums

enum class PersonalRecordStatisticsType(val typeName: String) : StatisticsType {
    MAX_WEIGHT("Max Weight"),
    MAX_REPS("Max Reps"),
    TOTAL_REPS("Total Reps"),
    TOTAL_SETS("Total Sets"),;

    override fun toString(): String {
        return typeName
    }

    override fun equals(other: StatisticsType): Boolean {
        return this.toString() == other.toString()
    }
}