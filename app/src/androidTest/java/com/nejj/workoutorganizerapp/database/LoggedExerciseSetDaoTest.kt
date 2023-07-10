package com.nejj.workoutorganizerapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.nejj.workoutorganizerapp.getOrAwaitValue
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class LoggedExerciseSetDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var loggedExerciseSetDao: LoggedExerciseSetDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()

        loggedExerciseSetDao = database.getLoggedExerciseSetDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertLoggedExerciseSet() = runTest {
        val loggedExerciseSet = LoggedExerciseSet(1, 1, 1, 50.0, 10, 1, false, "Set 1", "")
        loggedExerciseSetDao.upsert(loggedExerciseSet)

        val allLoggedExerciseSets = loggedExerciseSetDao.getAllEntities().getOrAwaitValue()

        assertThat(allLoggedExerciseSets).contains(loggedExerciseSet)
    }

    @Test
    fun deleteLoggedExerciseSet() = runTest {
        val loggedExerciseSet = LoggedExerciseSet(1, 1, 1, 50.0, 10, 1, false, "Set 1", "")
        loggedExerciseSetDao.upsert(loggedExerciseSet)
        loggedExerciseSetDao.deleteEntity(loggedExerciseSet)

        val allLoggedExerciseSets = loggedExerciseSetDao.getAllEntities().getOrAwaitValue()

        assertThat(allLoggedExerciseSets).doesNotContain(loggedExerciseSet)
    }

    @Test
    fun updateLoggedExerciseSetUID() = runTest {
        val newUID = "newUID"
        val loggedExerciseSet1 = LoggedExerciseSet(null, 1, 1, 50.0, 10, 1, false, "Set 1", "firstUID")
        val loggedExerciseSet2 = LoggedExerciseSet(null, 1, 2, 60.0, 12, 2, false, "Set 2", "secondUID")
        loggedExerciseSetDao.upsert(loggedExerciseSet1)
        loggedExerciseSetDao.upsert(loggedExerciseSet2)

        loggedExerciseSetDao.updateLoggedExerciseSetsUserUID(newUID)
        val allLoggedExerciseSets = loggedExerciseSetDao.getAllEntities().getOrAwaitValue()
        // Check if all logged exercise sets have the userUID equal to newUID
        val allLoggedExerciseSetsHaveNewUID = allLoggedExerciseSets.all { it.userUID == newUID }
        assertTrue(allLoggedExerciseSetsHaveNewUID)
    }

    @Test
    fun getMaxOrder() = runTest {
        val newUID = "newUID"
        val loggedExerciseSet1 = LoggedExerciseSet(null, 1, 1, 50.0, 10, 1, false, "Set 1", "firstUID")
        val loggedExerciseSet2 = LoggedExerciseSet(null, 1, 1, 60.0, 12, 2, false, "Set 2", "secondUID")
        loggedExerciseSetDao.upsert(loggedExerciseSet1)
        loggedExerciseSetDao.upsert(loggedExerciseSet2)

        val maxOrder = loggedExerciseSetDao.getMaxOrder(1)
        assertThat(maxOrder).isEqualTo(2)
    }

    @Test
    fun getMaxOrderEmpty() = runTest {
        val maxOrder = loggedExerciseSetDao.getMaxOrder(1)
        assertThat(maxOrder).isNull()
    }

    @Test
    fun getExerciseSetForRoutineSet() = runTest {
        val loggedExerciseSet1 = LoggedExerciseSet(1, 1, 1, 50.0, 10, 1, false, "Set 1", "firstUID")
        val loggedExerciseSet2 = LoggedExerciseSet(2, 1, 1, 60.0, 12, 2, false, "Set 2", "secondUID")
        loggedExerciseSetDao.upsert(loggedExerciseSet1)
        loggedExerciseSetDao.upsert(loggedExerciseSet2)

        val exerciseSets = loggedExerciseSetDao.getExerciseSetsForLoggedRoutineSet(1)
        assertThat(exerciseSets).contains(loggedExerciseSet1)
        assertThat(exerciseSets).contains(loggedExerciseSet2)
    }
}
