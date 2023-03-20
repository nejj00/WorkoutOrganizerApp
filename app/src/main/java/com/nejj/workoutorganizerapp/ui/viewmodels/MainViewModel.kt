package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository

open class MainViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app) {
}