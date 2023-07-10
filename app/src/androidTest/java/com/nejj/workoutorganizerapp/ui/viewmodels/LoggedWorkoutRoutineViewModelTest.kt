package com.nejj.workoutorganizerapp.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.nejj.workoutorganizerapp.database.WorkoutDatabase
import com.nejj.workoutorganizerapp.getOrAwaitValue
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.LoggedWorkoutRoutine
import com.nejj.workoutorganizerapp.models.RoutineSet
import com.nejj.workoutorganizerapp.models.WorkoutRoutine
import com.nejj.workoutorganizerapp.models.relations.RoutineSetsWithExercise
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoggedWorkoutRoutineViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: WorkoutDatabase
    private lateinit var workoutRepository: WorkoutRepository
    private lateinit var loggedWorkoutRoutineViewModel: LoggedWorkoutRoutineViewModel

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WorkoutDatabase::class.java
        ).allowMainThreadQueries().build()

        workoutRepository = WorkoutRepository(database)
        loggedWorkoutRoutineViewModel = LoggedWorkoutRoutineViewModel(workoutRepository)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun initializeLoggedWorkoutRoutine() = runTest {
        val workoutRoutine = WorkoutRoutine(1, "new", "", true, "")

        val exercise1 = Exercise(1, 1, "new1", "", false, true)
        val exercise2 = Exercise(2, 2, "new2", "", false, true)
        val routineSet1 = RoutineSet(1, 1, 1, 0, 1, 1, true, "")
        val routineSet2 = RoutineSet(2, 1, 2, 0, 1, 2, true, "")

        val routineSetsWithExercises = mutableListOf<RoutineSetsWithExercise>()
        routineSetsWithExercises.add(RoutineSetsWithExercise(routineSet1, exercise1))
        routineSetsWithExercises.add(RoutineSetsWithExercise(routineSet2, exercise2))

        val loggedWorkoutRoutineWithLoggedRoutineSets = loggedWorkoutRoutineViewModel
            .initializeLoggedWorkoutRoutine(workoutRoutine, routineSetsWithExercises).getOrAwaitValue()

        assertThat(loggedWorkoutRoutineWithLoggedRoutineSets.loggedWorkoutRoutine.name)
            .isEqualTo(workoutRoutine.name)
        assertThat(loggedWorkoutRoutineWithLoggedRoutineSets.loggedRoutineSets.size)
            .isEqualTo(routineSetsWithExercises.size)
    }
}
