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
import com.nejj.workoutorganizerapp.models.Exercise
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
class LoggedRoutineSetViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var workoutRepository: WorkoutRepository
    private lateinit var loggedRoutineSetViewModel: LoggedRoutineSetViewModel

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()

        workoutRepository = WorkoutRepository(database)
        loggedRoutineSetViewModel = LoggedRoutineSetViewModel(workoutRepository)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertNewLoggedRoutineSet() = runTest {
        val exercise = Exercise(1, 1, "name", "type", false, true, "")
        val loggedRoutineId = 1L

        loggedRoutineSetViewModel.insertNewLoggedRoutineSet(loggedRoutineId, exercise)

        val loggedRoutineSet = LoggedRoutineSet()
        loggedRoutineSet.loggedRoutineSetId = loggedRoutineId
        loggedRoutineSet.loggedRoutineId = loggedRoutineId
        loggedRoutineSet.exerciseId = exercise.exerciseId
        loggedRoutineSet.exerciseName = exercise.name
        loggedRoutineSet.setsOrder = 1
        loggedRoutineSet.setsCount = 1
        loggedRoutineSet.userUID = Firebase.auth.currentUser?.uid

        val loggedExerciseSet = LoggedExerciseSet()
        loggedExerciseSet.loggedExerciseSetId = 1
        loggedExerciseSet.loggedRoutineId = loggedRoutineId
        loggedExerciseSet.loggedRoutineSetId = 1
        loggedExerciseSet.userUID = Firebase.auth.currentUser?.uid

        val loggedRoutineSets = workoutRepository.getLoggedRoutineSets().getOrAwaitValue()
        val loggedExerciseSets = workoutRepository.getLoggedExerciseSets().getOrAwaitValue()

        assertThat(loggedRoutineSets).contains(loggedRoutineSet)
        assertThat(loggedExerciseSets).contains(loggedExerciseSet)
    }
}
