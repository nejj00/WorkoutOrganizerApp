package com.nejj.workoutorganizerapp.models.converters

import androidx.room.TypeConverter
import java.time.LocalTime

class LocalTimeConverter {

    @TypeConverter
    fun fromStringToTime(value: String?): LocalTime? {
        return value?.let { LocalTime.parse(it) }
    }

    @TypeConverter
    fun fromTimeToString(time: LocalTime?): String? {
        return time?.toString()
    }
}