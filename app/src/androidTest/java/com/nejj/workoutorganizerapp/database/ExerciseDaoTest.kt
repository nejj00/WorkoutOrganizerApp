package com.nejj.workoutorganizerapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.nejj.workoutorganizerapp.getOrAwaitValue
import com.nejj.workoutorganizerapp.models.Exercise
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ExerciseDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var exerciseDao: ExerciseDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()

        exerciseDao = database.getExerciseDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertExercise() = runTest {
        val exercise = Exercise(null, 0, "exerciseDescription", "exerciseType", false, true, "")
        exerciseDao.upsert(exercise)

        val allExercise = exerciseDao.getAllEntities().getOrAwaitValue()

        assertThat(allExercise).contains(exercise)
    }

    @Test
    fun deleteExercise() = runTest {
        val exercise = Exercise(1, 0, "exerciseDescription", "exerciseType", false, true, "")
        exerciseDao.upsert(exercise)
        exerciseDao.deleteEntity(exercise)

        val allExercise = exerciseDao.getAllEntities().getOrAwaitValue()

        assertThat(allExercise).doesNotContain(exercise)
    }

    @Test
    fun updateExercisesUID() = runTest {
        val newUID = "newUID"
        val exercise1 = Exercise(null, 0, "exerciseDescription", "exerciseType", false, true, "firsUID")
        val exercise2 = Exercise(null, 0, "exerciseDescription", "exerciseType", false, true, "firstUID")
        exerciseDao.upsert(exercise1)
        exerciseDao.upsert(exercise2)

        exerciseDao.updateExercisesUserUID(newUID)
        val allExercise = exerciseDao.getAllEntities().getOrAwaitValue()
        // Check if all exercises have the UID equal to newUID
        val allExercisesHaveNewUID = allExercise.all { it.userUID == newUID }
        assertTrue(allExercisesHaveNewUID)
    }
}