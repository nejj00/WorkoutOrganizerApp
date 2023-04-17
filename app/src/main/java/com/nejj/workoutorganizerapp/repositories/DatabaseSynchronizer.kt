package com.nejj.workoutorganizerapp.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.nejj.workoutorganizerapp.util.Resource
import kotlinx.coroutines.Job
import retrofit2.Response

class DatabaseSynchronizer {

    private val TAG = "DatabaseSynchronizer"

    suspend fun <T, Y> updateLocalDatabase(response: Response<Y>, getEntitiesList: (entitiesResponse: Y) -> List<T>, liveData: LiveData<List<T>>, insertEntity: suspend (entity: T) -> Job) {
        val entitiesApi = mutableListOf<T>()
        val result = handleResponse(response)
        when(result) {
            is Resource.Success -> {
                result.data?.let { entitiesResponse ->
                    entitiesApi.addAll( getEntitiesList(entitiesResponse) )
                }
            }
            is Resource.Error -> {
                result.message?.let { message ->
                    Log.e(TAG, "An error occured: $message")
                }
            }
            is Resource.Loading -> {
            }
        }
        val entitiesLocal = mutableListOf<T>()
        liveData.observeForever(Observer { entities ->
            entitiesLocal.addAll(entities)
        })
        if(entitiesLocal.containsAll(entitiesApi)) {
            Log.d(TAG, "Local database is up to date")
        } else {
            Log.d(TAG, "Local database is not up to date")
            entitiesApi.forEach { entity ->
                if(!entitiesLocal.contains(entity)) {
                    insertEntity(entity)
                }
            }
        }
    }

    private fun <Y> handleResponse(response: Response<Y>): Resource<Y> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}