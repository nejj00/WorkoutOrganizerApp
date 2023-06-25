package com.nejj.workoutorganizerapp.util

import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class StringFormatter {

    companion object {
        fun formatDouble(number: Double): String {
            val format = DecimalFormat("#.##") // Define the decimal format pattern

            // Remove decimal places if the number is a whole number
            format.isDecimalSeparatorAlwaysShown = false

            return format.format(number)
        }

        fun formatLocalDate(date: LocalDate): String {
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)
            return date.format(formatter)
        }
    }


}