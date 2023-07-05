package com.nejj.workoutorganizerapp.models.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter {

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let {
            return LocalDateTime.parse(value, formatter)
        }
    }

    @TypeConverter
    fun toTimestamp(value: LocalDateTime?): String? {
        return value?.format(formatter)
    }
}