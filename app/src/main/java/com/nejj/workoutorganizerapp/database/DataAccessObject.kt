package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nejj.workoutorganizerapp.models.ExerciseCategory

interface DataAccessObject<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: T): Long

    @Delete
    suspend fun deleteEntity(entity: T)
}