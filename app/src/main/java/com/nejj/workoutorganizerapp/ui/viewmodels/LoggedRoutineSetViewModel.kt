package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nejj.workoutorganizerapp.models.LoggedRoutineSet
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import kotlinx.coroutines.launch

class LoggedRoutineSetViewModel(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app) {

    fun insertEntity(entity: LoggedRoutineSet) = viewModelScope.launch {
        workoutRepository.upsertLoggedRoutineSet(entity)
    }

    fun deleteEntity(entity: LoggedRoutineSet) = viewModelScope.launch {
        workoutRepository.deleteLoggedRoutineSet(entity)
    }
}