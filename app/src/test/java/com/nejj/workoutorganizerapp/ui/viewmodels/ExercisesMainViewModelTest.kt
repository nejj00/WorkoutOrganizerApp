package com.nejj.workoutorganizerapp.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nejj.workoutorganizerapp.models.Exercise
import com.nejj.workoutorganizerapp.models.RoutineSet
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever
import org.junit.Assert.*
import org.mockito.Mockito.verify

class ExercisesMainViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var workoutRepository: WorkoutRepository
    private lateinit var viewModel: ExercisesMainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = ExercisesMainViewModel(workoutRepository)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testInsertEntity() = runTest {
        val exercise = Exercise()
        launch { viewModel.insertEntity(exercise) }
        verify(workoutRepository).upsertExercise(exercise)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testCheckIfExerciseIsUsed() = runTest {
        val exercise = Exercise()
        val routineSetsLiveData = listOf(RoutineSet())

        whenever(workoutRepository.getRoutineSetsByExerciseId(exercise.exerciseId!!))
            .thenReturn(routineSetsLiveData)

        val liveData = viewModel.checkIfExerciseIsUsed(exercise)

        assertEquals(true, liveData.value)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testDeleteEntity() = runTest {
        val exercise = Exercise()
        launch { viewModel.deleteEntity(exercise) }
        verify(workoutRepository).deleteExercise(exercise)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testDeleteExerciseWithRoutineSets() = runTest {
        val exercise = Exercise()
        launch { viewModel.deleteExerciseWithRoutineSets(exercise) }
        verify(workoutRepository).deleteRoutineSetsByExerciseId(exercise.exerciseId!!)
        verify(workoutRepository).deleteExercise(exercise)
    }

    // You can add more tests for the other functions.
}
