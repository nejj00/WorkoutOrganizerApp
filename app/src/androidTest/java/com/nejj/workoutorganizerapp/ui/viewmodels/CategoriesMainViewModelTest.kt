package com.nejj.workoutorganizerapp.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.nejj.workoutorganizerapp.database.WorkoutDatabase
import com.nejj.workoutorganizerapp.getOrAwaitValue
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoriesMainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var workoutRepository: WorkoutRepository
    private lateinit var exerciseCategoryViewModel: CategoriesMainViewModel

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()

        workoutRepository = WorkoutRepository(database)
        exerciseCategoryViewModel = CategoriesMainViewModel(workoutRepository)
    }

    @Test
    fun insertExerciseCategory() = runBlocking {
        val exerciseCategory = ExerciseCategory(null, "categoryName", true, "")
        exerciseCategoryViewModel.insertEntity(exerciseCategory)

        val allExerciseCategories = exerciseCategoryViewModel.getEntities().getOrAwaitValue()

        assertThat(allExerciseCategories).contains(exerciseCategory)
    }

    @Test
    fun checkIfExerciseCategoryIsUsedTrue() = runBlocking {
        val exerciseCategory = ExerciseCategory(1, "categoryName", true, "")
        exerciseCategoryViewModel.insertEntity(exerciseCategory)

        workoutRepository.upsertExercise(
            Exercise(1, 1, "exerciseDescription", "exerciseType", false, true, "")
        )

        val isUsed = exerciseCategoryViewModel.checkIfCategoryIsUsed(exerciseCategory).getOrAwaitValue()

        assertThat(isUsed).isTrue()
    }

    @Test
    fun checkIfExerciseCategoryIsUsedFalse() = runBlocking {
        val exerciseCategory = ExerciseCategory(1, "categoryName", true, "")
        exerciseCategoryViewModel.insertEntity(exerciseCategory)

        val isUsed = exerciseCategoryViewModel.checkIfCategoryIsUsed(exerciseCategory).getOrAwaitValue()

        assertThat(isUsed).isFalse()
    }
}