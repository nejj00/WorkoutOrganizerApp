package com.nejj.workoutorganizerapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.nejj.workoutorganizerapp.models.User

@Dao
interface UserDao : DataAccessObject<User> {

    @Query("SELECT * FROM users")
    fun getAllEntities(): LiveData<List<User>>

    @Query("SELECT * FROM users")
    suspend fun getAllEntitiesList(): List<User>

    @Query("SELECT * FROM users ORDER BY userLastLogin DESC LIMIT 1")
    suspend fun getMostRecentUser(): User?

    @Query("SELECT * FROM users WHERE userUID = :userUID")
    suspend fun getUserByUID(userUID: String): User?
}