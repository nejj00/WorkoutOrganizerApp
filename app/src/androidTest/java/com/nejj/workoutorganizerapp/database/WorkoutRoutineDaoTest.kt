package com.nejj.workoutorganizerapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.nejj.workoutorganizerapp.getOrAwaitValue
import com.nejj.workoutorganizerapp.models.WorkoutRoutine
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class WorkoutRoutineDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var workoutRoutineDao: WorkoutRoutineDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()

        workoutRoutineDao = database.getWorkoutRoutineDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertWorkoutRoutine() = runTest {
        val workoutRoutine = WorkoutRoutine(1, "Routine 1", "Some notes", false, "")
        workoutRoutineDao.upsert(workoutRoutine)

        val allWorkoutRoutines = workoutRoutineDao.getAllEntities().getOrAwaitValue()

        assertThat(allWorkoutRoutines).contains(workoutRoutine)
    }

    @Test
    fun deleteWorkoutRoutine() = runTest {
        val workoutRoutine = WorkoutRoutine(1, "Routine 1", "Some notes", false, "")
        workoutRoutineDao.upsert(workoutRoutine)
        workoutRoutineDao.deleteEntity(workoutRoutine)

        val allWorkoutRoutines = workoutRoutineDao.getAllEntities().getOrAwaitValue()

        assertThat(allWorkoutRoutines).doesNotContain(workoutRoutine)
    }

    @Test
    fun updateWorkoutRoutineUID() = runTest {
        val newUID = "newUID"
        val workoutRoutine1 = WorkoutRoutine(null, "Routine 1", "Some notes", false, "firstUID")
        val workoutRoutine2 = WorkoutRoutine(null, "Routine 2", "Other notes", false, "secondUID")
        workoutRoutineDao.upsert(workoutRoutine1)
        workoutRoutineDao.upsert(workoutRoutine2)

        workoutRoutineDao.updateWorkoutRoutinesUserUID(newUID)
        val allWorkoutRoutines = workoutRoutineDao.getAllEntities().getOrAwaitValue()
        // Check if all workout routines have the userUID equal to newUID
        val allWorkoutRoutinesHaveNewUID = allWorkoutRoutines.all { it.userUID == newUID }
        assertTrue(allWorkoutRoutinesHaveNewUID)
    }
}
