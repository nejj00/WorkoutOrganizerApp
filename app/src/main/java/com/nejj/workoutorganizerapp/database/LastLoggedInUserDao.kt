package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.LastLoggedInUser

@Dao
interface LastLoggedInUserDao : DataAccessObject<LastLoggedInUser> {

    @Query("SELECT * FROM last_logged_in_user")
    fun getAllEntities(): LiveData<List<LastLoggedInUser>>

    @Query("SELECT * FROM last_logged_in_user")
    suspend fun getAllEntitiesList(): List<LastLoggedInUser>
}