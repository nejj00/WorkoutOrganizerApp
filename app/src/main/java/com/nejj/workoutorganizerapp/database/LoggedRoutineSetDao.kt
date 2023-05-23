package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet

@Dao
interface LoggedRoutineSetDao : DataAccessObject<LoggedRoutineSet> {

    @Query("SELECT * FROM logged_routine_sets")
    fun getAllEntities(): LiveData<List<LoggedRoutineSet>>

}