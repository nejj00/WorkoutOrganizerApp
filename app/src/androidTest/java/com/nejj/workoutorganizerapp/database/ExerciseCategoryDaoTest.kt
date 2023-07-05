package com.nejj.workoutorganizerapp.database

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.nejj.workoutorganizerapp.getOrAwaitValue
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ExerciseCategoryDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var exerciseCategoryDao: ExerciseCategoryDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()

        exerciseCategoryDao = database.getExerciseCategoryDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertExerciseCategory() = runTest {
        val exerciseCategory = ExerciseCategory(null, "Category 1", false, "")
        exerciseCategoryDao.upsert(exerciseCategory)

        val allExerciseCategories = exerciseCategoryDao.getAllEntities().getOrAwaitValue()

        assertThat(allExerciseCategories).contains(exerciseCategory)
    }

    @Test
    fun deleteExerciseCategory() = runTest {
        val exerciseCategory = ExerciseCategory(1, "Category 1", false, "")
        exerciseCategoryDao.upsert(exerciseCategory)
        exerciseCategoryDao.deleteEntity(exerciseCategory)

        val allExerciseCategories = exerciseCategoryDao.getAllEntities().getOrAwaitValue()

        assertThat(allExerciseCategories).doesNotContain(exerciseCategory)
    }

    @Test
    fun updateExerciseCategoryUID() = runTest {
        val newUID = "newUID"
        val exerciseCategory1 = ExerciseCategory(null, "Category 1", false, "firstUID")
        val exerciseCategory2 = ExerciseCategory(null, "Category 2", false, "secondUID")
        exerciseCategoryDao.upsert(exerciseCategory1)
        exerciseCategoryDao.upsert(exerciseCategory2)

        exerciseCategoryDao.updateCategoriesUserUID(newUID)
        val allExerciseCategories = exerciseCategoryDao.getAllEntities().getOrAwaitValue()
        // Check if all exercise categories have the userUID equal to newUID
        val allExerciseCategoriesHaveNewUID = allExerciseCategories.all { it.userUID == newUID }
        assertTrue(allExerciseCategoriesHaveNewUID)
    }
}
