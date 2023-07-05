package com.nejj.workoutorganizerapp.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.nejj.workoutorganizerapp.database.WorkoutDatabase
import com.nejj.workoutorganizerapp.getOrAwaitValue
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExercisesMainViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var workoutRepository: WorkoutRepository
    private lateinit var exercisesMainViewModel: ExercisesMainViewModel

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()

        workoutRepository = WorkoutRepository(database)
        exercisesMainViewModel = ExercisesMainViewModel(workoutRepository)
    }

    @Test
    fun insertExercise() = runTest {
        val exercise = Exercise(null, 0, "exerciseDescription", "exerciseType", false, true, "")
        exercisesMainViewModel.insertEntity(exercise)

        val allExercise = exercisesMainViewModel.getEntities().getOrAwaitValue()

        assertThat(allExercise).contains(exercise)
    }
}