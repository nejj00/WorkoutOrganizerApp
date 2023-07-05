package com.nejj.workoutorganizerapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.nejj.workoutorganizerapp.getOrAwaitValue
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class LoggedRoutineSetDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var loggedRoutineSetDao: LoggedRoutineSetDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()

        loggedRoutineSetDao = database.getLoggedRoutineSetDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertLoggedRoutineSet() = runTest {
        val loggedRoutineSet = LoggedRoutineSet(1, 1, 1, "Exercise 1", 2, 3, 1, "")
        loggedRoutineSetDao.upsert(loggedRoutineSet)

        val allLoggedRoutineSets = loggedRoutineSetDao.getAllEntities().getOrAwaitValue()

        assertThat(allLoggedRoutineSets).contains(loggedRoutineSet)
    }

    @Test
    fun deleteLoggedRoutineSet() = runTest {
        val loggedRoutineSet = LoggedRoutineSet(1, 1, 1, "Exercise 1", 2, 3, 1, "")
        loggedRoutineSetDao.upsert(loggedRoutineSet)
        loggedRoutineSetDao.deleteEntity(loggedRoutineSet)

        val allLoggedRoutineSets = loggedRoutineSetDao.getAllEntities().getOrAwaitValue()

        assertThat(allLoggedRoutineSets).doesNotContain(loggedRoutineSet)
    }

    @Test
    fun updateLoggedRoutineSetUID() = runTest {
        val newUID = "newUID"
        val loggedRoutineSet1 = LoggedRoutineSet(null, 1, 1, "Exercise 1", 2, 3, 1, "firstUID")
        val loggedRoutineSet2 = LoggedRoutineSet(null, 1, 2, "Exercise 2", 1, 2, 2, "secondUID")
        loggedRoutineSetDao.upsert(loggedRoutineSet1)
        loggedRoutineSetDao.upsert(loggedRoutineSet2)

        loggedRoutineSetDao.updateLoggedRoutineSetsUserUID(newUID)
        val allLoggedRoutineSets = loggedRoutineSetDao.getAllEntities().getOrAwaitValue()
        // Check if all logged routine sets have the userUID equal to newUID
        val allLoggedRoutineSetsHaveNewUID = allLoggedRoutineSets.all { it.userUID == newUID }
        assertTrue(allLoggedRoutineSetsHaveNewUID)
    }
}
