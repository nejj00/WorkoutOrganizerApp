package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository

class BasicViewModelProviderFactory(
    val app: Application,
    private val workoutRepository: WorkoutRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ExercisesMainViewModel::class.java) -> {
                ExercisesMainViewModel(app, workoutRepository) as T
            }
            modelClass.isAssignableFrom(CategoriesMainViewModel::class.java) -> {
                CategoriesMainViewModel(app, workoutRepository) as T
            }
            modelClass.isAssignableFrom(WorkoutRoutineMainViewModel::class.java) -> {
                WorkoutRoutineMainViewModel(app, workoutRepository) as T
            }
            modelClass.isAssignableFrom(RoutineSetMainViewModel::class.java) -> {
                RoutineSetMainViewModel(app, workoutRepository) as T
            }
            modelClass.isAssignableFrom(LoggedWorkoutRoutineViewModel::class.java) -> {
                LoggedWorkoutRoutineViewModel(app, workoutRepository) as T
            }
            modelClass.isAssignableFrom(LoggedRoutineSetViewModel::class.java) -> {
                LoggedRoutineSetViewModel(app, workoutRepository) as T
            }
            modelClass.isAssignableFrom(LoggedExerciseSetViewModel::class.java) -> {
                LoggedExerciseSetViewModel(app, workoutRepository) as T
            }
            modelClass.isAssignableFrom(LastLoggedInUserViewModel::class.java) -> {
                LastLoggedInUserViewModel(app, workoutRepository) as T
            }
            modelClass.isAssignableFrom(StatisticsViewModel::class.java) -> {
                StatisticsViewModel(app, workoutRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}