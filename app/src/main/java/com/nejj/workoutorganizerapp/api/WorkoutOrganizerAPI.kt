package com.nejj.workoutorganizerapp.api

import com.nejj.workoutorganizerapp.models.CategoriesResponse
import retrofit2.Response
import retrofit2.http.GET

interface WorkoutOrganizerAPI {

    @GET("api/categories")
    suspend fun getCategories() : Response<CategoriesResponse>

}