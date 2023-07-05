package com.nejj.workoutorganizerapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.nejj.workoutorganizerapp.getOrAwaitValue
import com.nejj.workoutorganizerapp.models.LoggedWorkoutRoutine
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import java.time.LocalDate
import java.time.LocalTime

@RunWith(AndroidJUnit4::class)
@SmallTest
class LoggedWorkoutRoutineDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var loggedWorkoutRoutineDao: LoggedWorkoutRoutineDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()

        loggedWorkoutRoutineDao = database.getLoggedWorkoutRoutineDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertLoggedWorkoutRoutine() = runTest {
        val loggedWorkoutRoutine = LoggedWorkoutRoutine(1, "Routine 1", 70.0, "Some notes", LocalDate.now(), LocalTime.now(), null, "")
        loggedWorkoutRoutineDao.upsert(loggedWorkoutRoutine)

        val allLoggedWorkoutRoutines = loggedWorkoutRoutineDao.getAllEntities().getOrAwaitValue()

        assertThat(allLoggedWorkoutRoutines).contains(loggedWorkoutRoutine)
    }

    @Test
    fun deleteLoggedWorkoutRoutine() = runTest {
        val loggedWorkoutRoutine = LoggedWorkoutRoutine(1, "Routine 1", 70.0, "Some notes", LocalDate.now(), LocalTime.now(), null, "")
        loggedWorkoutRoutineDao.upsert(loggedWorkoutRoutine)
        loggedWorkoutRoutineDao.deleteEntity(loggedWorkoutRoutine)

        val allLoggedWorkoutRoutines = loggedWorkoutRoutineDao.getAllEntities().getOrAwaitValue()

        assertThat(allLoggedWorkoutRoutines).doesNotContain(loggedWorkoutRoutine)
    }

    @Test
    fun updateLoggedWorkoutRoutineUID() = runTest {
        val newUID = "newUID"
        val loggedWorkoutRoutine1 = LoggedWorkoutRoutine(null, "Routine 1", 70.0, "Some notes", LocalDate.now(), LocalTime.now(), null, "firstUID")
        val loggedWorkoutRoutine2 = LoggedWorkoutRoutine(null, "Routine 2", 65.0, "Other notes", LocalDate.now(), LocalTime.now(), null, "secondUID")
        loggedWorkoutRoutineDao.upsert(loggedWorkoutRoutine1)
        loggedWorkoutRoutineDao.upsert(loggedWorkoutRoutine2)

        loggedWorkoutRoutineDao.updateLoggedWorkoutRoutinesUserUID(newUID)
        val allLoggedWorkoutRoutines = loggedWorkoutRoutineDao.getAllEntities().getOrAwaitValue()
        // Check if all logged workout routines have the userUID equal to newUID
        val allLoggedWorkoutRoutinesHaveNewUID = allLoggedWorkoutRoutines.all { it.userUID == newUID }
        assertTrue(allLoggedWorkoutRoutinesHaveNewUID)
    }
}
