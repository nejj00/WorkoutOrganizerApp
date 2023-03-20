package com.nejj.workoutorganizerapp.database

import androidx.room.*

interface DataAccessObject<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: T): Long

    @Delete
    suspend fun deleteEntity(entity: T)
}