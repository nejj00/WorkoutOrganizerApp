package com.nejj.workoutorganizerapp.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.nejj.workoutorganizerapp.models.ExerciseCategory
import com.nejj.workoutorganizerapp.repositories.WorkoutRepository
import com.nejj.workoutorganizerapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Response

abstract class MainViewModel<T, Y>(
    app: Application,
    val workoutRepository: WorkoutRepository
) : AndroidViewModel(app) {
    abstract fun insertEntity(entity: T): Job
    abstract fun getEntities(): LiveData<List<T>>
    abstract fun deleteEntity(entity: T): Job
    abstract suspend fun getEntitiesApi(): Response<Y>
}