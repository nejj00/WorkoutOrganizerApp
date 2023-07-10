package com.nejj.workoutorganizerapp.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.nejj.workoutorganizerapp.database.WorkoutDatabase
import com.nejj.workoutorganizerapp.getOrAwaitValue
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoggedExerciseSetViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var workoutRepository: WorkoutRepository
    private lateinit var loggedExerciseSetViewModel: LoggedExerciseSetViewModel

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()

        workoutRepository = WorkoutRepository(database)
        loggedExerciseSetViewModel = LoggedExerciseSetViewModel(workoutRepository)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertNewExerciseSetToLoggedRoutineSet() = runTest {
        val loggedRoutineSet = LoggedRoutineSet(1, 1, 1, "name", 0, 1, 1, "")

        loggedExerciseSetViewModel.insertNewExerciseSetToLoggedRoutineSet(loggedRoutineSet)

        val loggedExerciseSet = LoggedExerciseSet(
            1,
            loggedRoutineSet.loggedRoutineId,
            loggedRoutineSet.loggedRoutineSetId,
            0.0,
            0,
            1,
            false,
            "",
            Firebase.auth.uid.toString()
        )

        val exerciseSetAll = workoutRepository.getLoggedExerciseSets().getOrAwaitValue()

        assertThat(exerciseSetAll).contains(loggedExerciseSet)
    }
}