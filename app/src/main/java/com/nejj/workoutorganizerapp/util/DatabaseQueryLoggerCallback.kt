package com.nejj.workoutorganizerapp.util

import androidx.room.RoomDatabase

class DatabaseQueryLoggerCallback : RoomDatabase.QueryCallback {
    override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
        println("QUERY: $sqlQuery SQL Args: $bindArgs")
    }
}