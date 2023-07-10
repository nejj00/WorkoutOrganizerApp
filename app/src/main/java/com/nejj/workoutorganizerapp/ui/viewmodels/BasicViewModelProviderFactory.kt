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
                ExercisesMainViewModel(workoutRepository) as T
            }
            modelClass.isAssignableFrom(CategoriesMainViewModel::class.java) -> {
                CategoriesMainViewModel(workoutRepository) as T
            }
            modelClass.isAssignableFrom(WorkoutRoutineMainViewModel::class.java) -> {
                WorkoutRoutineMainViewModel(workoutRepository) as T
            }
            modelClass.isAssignableFrom(RoutineSetMainViewModel::class.java) -> {
                RoutineSetMainViewModel(workoutRepository) as T
            }
            modelClass.isAssignableFrom(LoggedWorkoutRoutineViewModel::class.java) -> {
                LoggedWorkoutRoutineViewModel(workoutRepository) as T
            }
            modelClass.isAssignableFrom(LoggedRoutineSetViewModel::class.java) -> {
                LoggedRoutineSetViewModel(workoutRepository) as T
            }
            modelClass.isAssignableFrom(LoggedExerciseSetViewModel::class.java) -> {
                LoggedExerciseSetViewModel(workoutRepository) as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(workoutRepository) as T
            }
            modelClass.isAssignableFrom(StatisticsViewModel::class.java) -> {
                StatisticsViewModel(workoutRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}