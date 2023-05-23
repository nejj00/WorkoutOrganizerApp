package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nejj.workoutorganizerapp.models.LoggedExerciseSet
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class LoggedExerciseSetViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app
) {

    fun insertEntity(entity: LoggedExerciseSet) = viewModelScope.launch {
        workoutRepository.upsertLoggedExerciseSet(entity)
    }

    fun deleteEntity(entity: LoggedExerciseSet) = viewModelScope.launch {
        workoutRepository.deleteLoggedExerciseSet(entity)
    }
}