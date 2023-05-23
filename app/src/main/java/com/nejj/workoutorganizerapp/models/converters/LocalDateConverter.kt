package com.nejj.workoutorganizerapp.models.converters

import androidx.room.TypeConverter
import java.time.LocalDate


class LocalDateConverter {

    @TypeConverter
    fun fromStringToDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }

    @TypeConverter
    fun fromDateToString(date: LocalDate?): String? {
        return date?.toString()
    }
}