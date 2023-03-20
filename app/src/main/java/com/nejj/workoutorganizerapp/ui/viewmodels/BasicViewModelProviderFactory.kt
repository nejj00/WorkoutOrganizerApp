package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository

class BasicViewModelProviderFactory(
    val app: Application,
    val workoutRepository: WorkoutRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CategoriesMainViewModel(app, workoutRepository) as T
    }
}